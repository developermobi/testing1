package com.mobisoft.sms.dao;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.OtpValidate;
import com.mobisoft.sms.model.Product;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.SmsDnd;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserProduct;
import com.mobisoft.sms.utility.EmailAPI;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.StringEncryption;
import com.mysql.jdbc.PreparedStatement;

@Repository("smsHelperDao")
public class SmsHelperDaoImpl implements SmsHelperDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/*@Autowired
	@Qualifier("sessionFactory2")
	SessionFactory sessionFactory2;*/
	@Autowired
	private EmailAPI emailApi;
	
	@Value("${supportEmail}")
	private String supportEmail;
	
	@Value("${sms_username}")
	private String userName;
	
	@Value("${password}")
	private String password;
	
	@Value("${senderId}")
	private String senderId;
	
	Cipher ecipher ;
	Cipher dcipher;
	
	private Session session = null;
	
	private Transaction tx = null;

	@Override
	public List<Integer> getBalance(int userId, int productId) {
		
		final Session session =  sessionFactory.openSession();
		List list = null;
		try {
			String sqlQuery = "select balance from sms_balance where user_id = "+userId+" and product_id ="+productId;
			Query query = session.createSQLQuery(sqlQuery);
			 list = query.list();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int deductBalanceDeleteUser(int userId, int resellerId, String remark, int deductType,Session session,Transaction tx) {
		int temp = 0;
	   
		try {
			User user=(User)session.get(User.class, userId);
			System.out.println("Address "+user.getAddress());
			Criteria criteria = session.createCriteria(UserProduct.class);
			
			criteria.add(Restrictions.eq("userId",user));
			List<UserProduct> productList = criteria.list();
			System.out.println("productId"+productList.size());
			
			Set<Product> products = new HashSet<Product>();
	        System.out.println(products.size());
	        
	        for (UserProduct userProduct1 : productList) {

	           System.out.println("User product"+userProduct1.getProductId().getId());
	           List<Integer> balanceList = getBalance(user.getUserId(),userProduct1.getProductId().getId());
	           System.out.println(balanceList);
	           temp = creditBalance(resellerId, userProduct1.getProductId().getId(), resellerId, balanceList.get(0), remark, deductType,session,tx);
	       	   temp = debitBalnce(resellerId, userProduct1.getProductId().getId(), userId, balanceList.get(0), remark, deductType,session,tx);
	       	
	        }
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		
		return temp;
	}

	@Override
	public int creditBalance(int creditUserId, int productId, int creditByUserId, int balance,String remark,int creditType,Session session,Transaction tx) {
		int newBalance =0; 
		try {
			Product product = (Product)session.get(Product.class,productId);
			User user=(User)session.get(User.class, creditUserId);
			List<Integer> balanceList = getBalance(user.getUserId(),product.getId());
			newBalance = balanceList.get(0)+balance;
			Credit credit =new Credit();
			credit.setCredit(balance);
			credit.setCreditBy(creditByUserId);
			credit.setCurrentAmount(newBalance);
			credit.setRemark(remark);
			credit.setCreditType(creditType);
			credit.setPreviousAmouunt(balanceList.get(0));
			credit.setProductId(product);
			credit.setUserId(user);		
			session.saveOrUpdate(credit);
			updateUserBalance(newBalance, user.getUserId(), product.getId(), session, tx);
			
			//-------------------------email-----------------------
			//User resellerUser=(User)session.get(User.class, resellerId);
			String subject = "Credit User user name :-- "+user.getUserName();
			String msgBody = "New credit Balance is "+balance+"\n\n"
								+remark+"\n"
							  + "Thank You\n";
			
			Map<String, String> emailDetails = new HashMap<String, String>();
			emailDetails.put("toAddress", supportEmail);
			emailDetails.put("subject", subject);
			emailDetails.put("msgBody", msgBody);
			
			emailApi.sendSimpleMail(emailDetails);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBalance;
	}

	@Override
	public int debitBalnce(int creditUserId, int productId, int debitByUserId, int balance, String remark,int debitType,Session session,Transaction tx) {
		
		int temp =0;
		
		try {
			Product product = (Product)session.get(Product.class,productId);
			User resellerUser=(User)session.get(User.class, debitByUserId);
			
			List<Integer> balanceList = getBalance(resellerUser.getUserId(),product.getId());
			int newResellerBalance = balanceList.get(0)-balance;
			
			User user=(User)session.get(User.class, creditUserId);
			Debit debit = new Debit();
			debit.setDebit(balance);
			debit.setCurrentAmount(newResellerBalance);
			debit.setPreviousAmouunt(balanceList.get(0));
			debit.setDebitBy(resellerUser.getUserId());					
			debit.setRemark(remark);			
			debit.setDebitType(debitType);			
			debit.setUserId(resellerUser);
			debit.setProductId(product);
			
			session.saveOrUpdate(debit);
			temp = updateUserBalance(newResellerBalance, resellerUser.getUserId(), productId, session, tx);
			//-------------------------email-----------------------
			//User resellerUser=(User)session.get(User.class, resellerId);
			String subject = "Debit User  user name :-- "+resellerUser.getUserName();
			String msgBody = "Debit Balance is "+balance+"\n\n"
								+remark+"\n"						 
							  + "Thank You\n";
			
			Map<String, String> emailDetails = new HashMap<String, String>();
			emailDetails.put("toAddress", supportEmail);
			emailDetails.put("subject", subject);
			emailDetails.put("msgBody", msgBody);
			
			emailApi.sendSimpleMail(emailDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return temp;
	}

	@Override
	public int updateUserBalance(int newBalance, int userId, int productId,Session session,Transaction tx) {		
		int updateSmsBalanceResult = 0;
		try {
			String sqlquery = "update sms_balance set balance = "+newBalance+" where user_id= "+userId+" and product_id="+productId;
			Query query = session.createSQLQuery(sqlquery);
			updateSmsBalanceResult = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateSmsBalanceResult;
	}

	@Override
	public int creditOnUser(int newBalance, int userId, int productId, int creditInUser, int creditByReseller,
			Session session, Transaction tx) {
	/*	int temp =0;
		Product product = (Product)session.get(Product.class,productId);
		User user=(User)session.get(User.class, resellerId);
		List<Integer> balanceList = getBalance(user.getUserId(),product.getId());
		int oldBalance = balanceList.get(0)+newBalance;
		Credit credit =new Credit();
		credit.setCredit(balance);
		credit.setCreditBy(resellerId);
		credit.setCurrentAmount(newBalance);
		credit.setRemark(remark);
		credit.setCreditType(creditType);
		credit.setPreviousAmouunt(balanceList.get(0));
		credit.setProductId(product);
		credit.setUserId(user);		
		session.saveOrUpdate(credit);
		updateUserBalance(newBalance, user.getUserId(), product.getId(), session, tx);
		
		return newBalance;*/
		return 0;
	}

	@Override
	public  List<SmsBalance> findProdcut(int userId, int prodcutId) {
		session= sessionFactory.openSession();
		List<SmsBalance> list = null;
		try {
			User user =(User)session.load(User.class, userId);
			Product product =(Product)session.load(Product.class, prodcutId);
			Criteria criteria=session.createCriteria(SmsBalance.class);
			criteria.add(Restrictions.eq("userId", user)).add(Restrictions.eq("productId", product)).setFirstResult(0);
			list = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int messageCount(int messageType, int messageLenght) {
		int messageCount=0;
        try {
        	if(messageType == 1)
            {
                int part1Count = 160;
                int part2Count = 146;
                int part3Count = 153;
                if(messageLenght <= part1Count)
                {
                    messageCount =1;
                }
                else if(messageLenght <= (part1Count + part2Count))
                {
                    messageCount = 2;
                }
                else if(messageLenght > (part1Count+part2Count)){
                    double  count = Math.ceil(( messageLenght - part1Count - part2Count) / part3Count);
                    System.out.println(count);
                    messageCount = (int)(count + 3); 
                }           
                System.out.println("Text Message "+messageCount);
            }
            else
            {
                int part1Count = 70;
                int part2Count = 64;
                int part3Count = 67;
                if(messageLenght <= part1Count)
                {
                    messageCount =1;
                }
                else if(messageLenght <= (part1Count + part2Count))
                {
                    messageCount = 2;
                }
                else if(messageLenght > (part1Count+part2Count)){
                    double  count = Math.ceil(( messageLenght - part1Count - part2Count) / part3Count);
                    System.out.println(count);
                    messageCount = (int)(count + 3);                    
                }
                System.out.println("Unicode "+ messageCount);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return messageCount;
	}
/*	@Override
	public List<SmsDnd> filterDndNumber() {
		Session session = sessionFactory2.openSession();
		Transaction tx = session.beginTransaction();
		List<SmsDnd> list = session.createCriteria(SmsDnd.class).list();
		return list;
	}*/
	@Override
	public List<UserProduct> getRouteDetails(int userId, int productId) {
	
		final Session session= sessionFactory.openSession();
		List<UserProduct> userProductsList = null;
		try {
			User user=(User)session.get(User.class, userId);
			Product product= (Product)session.get(Product.class, productId);
			Criteria criteria= session.createCriteria(UserProduct.class);
			criteria.add(Restrictions.eq("userId", user))
			 		.add(Restrictions.eq("productId", product));
			userProductsList = criteria.list();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return userProductsList;
	}
	@Override
	public List<String> getGroupContact(String groupId, int userId) {
		session = sessionFactory.openSession();
		List<String> list = null;
		List<String> list1 = null;
		try {
			
			/*String sqlQuery1 = "SELECT id FROM group_details WHERE id in("+groupId+") and status =1 and user_id ="+userId;
			Query query1 = session.createSQLQuery(sqlQuery1);
			list1 = query1.list();*/
			//System.out.println("list to string "+list1.toString());
			String sqlQuery = "SELECT mobile FROM contact WHERE group_id in("+groupId+") and status =1 and user_id ="+userId;
			Query query = session.createSQLQuery(sqlQuery);
			list = query.list();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
		
	}
	@Override
	public List<UserAuthrization> getUserAuthrizationCheck(int userId,int productId) {
		session = sessionFactory.openSession();
		List<UserAuthrization> list = null;
		try {
			User user =(User)session.get(User.class, userId);
			Criteria criteria = session.createCriteria(UserAuthrization.class);
			criteria.add(Restrictions.eq("userId",user)).add(Restrictions.eq("productId", productId));		
			list = criteria.list();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}		
		return list;
	}
	@Override
	public List<Object> mobileNumber(final String mobileNumber) {
		
		session = sessionFactory.openSession();
		
		final List<Object> commonListData = new ArrayList<Object>();
		try {
			session.doWork(new Work() {				
			       @Override
			       public void execute(Connection conn) throws SQLException {

				          PreparedStatement pstmtDndFilter = null;
				          List<String> mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
				          List<String> listMatchDndData = new ArrayList<String>();
				          
				          try{
				           String sqlInsertDlrStatus = "INSERT INTO temp_dnd(mobile) VALUES (?)";
				           pstmtDndFilter = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus);
				           int i=0;
				           for(String mobile : mobileList){	
				        	   
				        	   if(mobile.length() > 5)
				        	   {
				        		   if(mobile.length() == 12)
					        	 	{
					        	 		mobile = mobile.substring(2);				        	 		
					        	 	}				        	
				        		   pstmtDndFilter.setString(1, mobile);
				        		   pstmtDndFilter.addBatch();
				        	   }
				        	   
				           }
				           conn.setAutoCommit(false);
				           pstmtDndFilter.executeBatch();
				        
				           String selectMobileNumberQuery ="select  Distinct t.mobile from  mobi_sms.temp_dnd t  inner join  sms_dnd.mobi_dnd d on t.mobile = d.mobile_no";
				           Statement statement = conn.createStatement();
				           ResultSet rs = statement.executeQuery(selectMobileNumberQuery);
				           while(rs.next())
				           {
				        	   String newMobile = rs.getString("mobile").toString();
				        	   StringBuilder s =  new StringBuilder(newMobile);
				        	   newMobile = s.insert(0,"91").toString();
				        	   listMatchDndData.add(newMobile);
				           }
				           ///Trancute table temp table
				           String trancuteTable ="TRUNCATE TABLE temp_dnd";
				           Statement statementTrancute = conn.createStatement();
				           ResultSet resultSetTrancute = statementTrancute.executeQuery(trancuteTable);
				           
				           conn.commit();
				           conn.setAutoCommit(true);
				        //  System.out.println("New Mobile list Size33:"+listMatchDndData);
				        //  System.out.println("old Mobile list Size33:"+mobileList);
				           List<String> newFilterMobileList = new ArrayList<>();
				           for(int j=0; j<mobileList.size(); j++)
				           {
				        	   if(mobileList.get(j).length() != 12)
				        	   {
				        		   if(mobileList.get(j).length() != 0)
				        		   {
				        			   StringBuilder s =  new StringBuilder(mobileList.get(j));
						        	   String add91Mobile = s.insert(0,"91").toString();
						        	   newFilterMobileList.add(add91Mobile);
				        		   }
				        	   }
				        	   else
				        	   {
				        		   if(mobileList.get(j).length() != 0)
				        		   {
				        			   newFilterMobileList.add(mobileList.get(j));
				        		   }				        		  
				        	   } 
				           }
				           newFilterMobileList.removeAll(listMatchDndData);
				           commonListData.add(0, newFilterMobileList);
				           commonListData.add(1, listMatchDndData.size());
				         }catch(Exception e){
				        	 e.printStackTrace();
				        	 conn.rollback();
				         }
				         finally{
				        	 try {
				        		if(pstmtDndFilter != null)
				        		{
				        			pstmtDndFilter .close();
				        		}
							} catch (Exception e2) {
								e2.printStackTrace();
							}try {
				        		if(conn != null)
				        		{
				        			conn .close();
				        		}
							} catch (Exception e2) {
								e2.printStackTrace();
							} 
				         }
			     }

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return commonListData;
	}

	@Override
	public int genrateOtp(int userId) {
		
		System.out.println("helooo dear");
		session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp =0;
		try {
			User user = (User)session.get(User.class, userId);
			Criteria criteria = session.createCriteria(OtpValidate.class);
			criteria.add(Restrictions.eq("userId", user));
			List<OtpValidate> listOtp = criteria.list();
			String otp = Global.randomString(5);
			String newOtp = "";
			if(listOtp.size() > 0)
			{
				//newOtp = listOtp.get(0).getOtp();
				String sqlquery = "update otp set otp_data = '"+otp+"' where user_id= "+userId;
				Query query = session.createSQLQuery(sqlquery);
				int updateOtpData = query.executeUpdate();
				System.out.println("return valuse "+updateOtpData);
				if(updateOtpData > 0)
				{
					tx.commit();
					//session.close();
					String message ="Dear Sir,  Send otp on  your register mobile number and email id "+otp;
					String mobile = user.getMobile();
					if( mobile.length() == 12)
					{
						mobile = mobile.substring(2);
					}
					
					
					String subject = "Send Otp For Change Password";
					
					
					Map<String, String> emailDetails = new HashMap<String, String>();
					emailDetails.put("toAddress", user.getEmail());
					emailDetails.put("subject", subject);
					emailDetails.put("msgBody", message);
					
					emailApi.sendSimpleMail(emailDetails);
					
					try {
						System.out.println("helookajsdhsa2342123 ");
						temp = Global.sendMessage(userName, password,mobile, senderId, message);
						
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}			
			}
			else
			{
				System.out.println("Inside else");
				OtpValidate otpValidate = new OtpValidate();
				otpValidate.setOtpData(otp);
				otpValidate.setUserId(user);
				session.save(otpValidate);
				tx.commit();
				//session.close();
				String message ="Dear Sir,  Send otp on  your register mobile number and Email ID "+otpValidate.getOtpData();
				
				String subject = "Send Otp For Change Password";			
				
				Map<String, String> emailDetails = new HashMap<String, String>();
				emailDetails.put("toAddress", user.getEmail());
				emailDetails.put("subject", subject);
				emailDetails.put("msgBody", message);
				
				emailApi.sendSimpleMail(emailDetails);
				
				String mobile = user.getMobile();
				if( mobile.length() == 12)
				{
					mobile = mobile.substring(2);
				}
				try {
					temp = Global.sendMessage(userName, password,mobile, senderId, message);					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return temp;
	}

	@Override
	public int varifyOtp(String otp,int userId) {
		session = sessionFactory.openSession();
		tx= session.beginTransaction();
		int temp =0;
		try {
			User user = (User)session.get(User.class,userId);
			Criteria criteria = session.createCriteria(OtpValidate.class);
			criteria.add(Restrictions.eq("userId",user)).add(Restrictions.eq("otpData",otp));
			List<OtpValidate> list = criteria.list();
			if(list.size() > 0)
			{
				if(list.get(0).getOtpData().equals(otp))
				{
					temp =1;
				}		
				tx.commit();
				//session.close();
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return temp;
	}

	@Override
	public String encriptPassword(String password) {
		Long l = new Date().getTime();
		String plainClientCredentials= "fucking you c "+l+":"+password;
		
		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
	    /*
	    String credentials = new String(Base64.decodeBase64(base64ClientCredentials));
	   
	    // credentials = username:password
	    String[] values = credentials.split(":");		
	    String userName = values[0];
        String newPassword = values[1];*/
		
		return base64ClientCredentials;
	}

}
