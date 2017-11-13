package com.mobisoft.sms.dao;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.Product;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserProduct;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.utility.EmailAPI;
import com.mobisoft.sms.utility.Global;


@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private EmailAPI emailApi;
	
	@Value("${supportEmail}")
	private String supportEmail;
	
	private Session session = null;
	
	private Transaction tx = null;
	
	@Value("${sms_username}")
	private String userName;
	

	@Value("${password}")
	private String password;
	
	
	

	@Value("${senderId}")
	private String senderId;
	
	@Autowired
	private SmsHelperService smsHelperService;
	
	public int saveUser(User user) {
		session =  sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {
			session.saveOrUpdate(user);
			temp = 1;
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
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
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUser() {
		session = sessionFactory.openSession();	
		List<User> list = null;
		try {
			list = session.createCriteria(User.class).list();
			session.close();
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
	//@Transactional(readOnly = true)
	//@javax.transaction.Transactional
	public List<User> getUserById(int userId) {

		Session session = sessionFactory.openSession();
		List results = null;
		try {
			String sql = "SELECT * FROM user_product WHERE user_id = :userId order by product_id desc";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(UserProduct.class);
			query.setParameter("userId", userId);
			results = query.list();
			session.flush();
			session.clear();
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
		return results;
	}
	@Override
	public int updateUser(User user) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {
			
			//String sql = "UPDATE User SET user_name = :userName,address = :address,city = :city,company_name = :companyName,country = :country,email = :email,mobile = :mobile, name = :name,role = :role,state = :state, status = :status WHERE id = :id";
			String sql = "UPDATE User SET address = :address,city = :city,company_name = :companyName,country = :country,email = :email,mobile = :mobile, name = :name,state = :state,role = :role WHERE id = :id";
			Query qry = session.createQuery(sql);
			//qry.setParameter("userName", user.getUserName());
			qry.setParameter("address", user.getAddress());
			qry.setParameter("city", user.getCity());
			qry.setParameter("companyName", user.getCompanyName());
			qry.setParameter("country", user.getCountry());
			qry.setParameter("email", user.getEmail());
			qry.setParameter("mobile", user.getMobile());
			qry.setParameter("name", user.getName());			
			qry.setParameter("state", user.getState());	
			qry.setParameter("role", user.getRole());
			qry.setParameter("id", user.getUserId());
			
			
			temp = qry.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
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
	public int deleteUser(int userId,int resellerId) {
		int temp = 0;
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		User user=(User)session.get(User.class, userId);
		
		if(user.getStatus() == 1)
		{
			try {
				String remark = "Delete User By Self "+" User Name Is"+ user.getUserName();
				temp = smsHelperService.deductBalanceDeleteUser(user.getUserId(),resellerId,  remark, 2,session,tx);
				System.out.println("akldasdkl"+temp);
				if(temp == 1)
				{
					int status =2;
					String sql = "UPDATE User set status = :status WHERE id = :userId";
					Query qry = session.createQuery(sql);
					qry.setParameter("status", status);
					qry.setParameter("userId", user.getUserId());
					temp = qry.executeUpdate();
					System.out.println(temp);
					temp=1;
					tx.commit();
					//-------------------------email-----------------------
					User resellerUser=(User)session.get(User.class, resellerId);
					String subject = "Delete user by user name :-- "+resellerUser.getUserName();
					String msgBody = "Delete User name is "+user.getName()+"\n\n"									 
									  + " and User Name: "+user.getUserName()+"\n"
									 
									  + "Thank You\n";
					
					Map<String, String> emailDetails = new HashMap<String, String>();
					emailDetails.put("toAddress", supportEmail);
					emailDetails.put("subject", subject);
					emailDetails.put("msgBody", msgBody);
					
					emailApi.sendSimpleMail(emailDetails);
					
					//session.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				temp = 2;
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
		}
		else
		{
			temp =2;
		}
		return temp;
		
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	@javax.transaction.Transactional
	public List<User> getUserByUserName(String userName) {

		Session session = sessionFactory.openSession();
		//Transaction tx = session.beginTransaction();
		List<User> list = null;
		try {
			
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("userName", userName));
			list = criteria.list();			
			session.flush();
			//tx.commit();
			session.clear();
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
	@SuppressWarnings("unchecked")
	public int getBalnce(int userId,int productId)
	{
		int balance = 0;
		session = sessionFactory.openSession();
		List<SmsBalance> list= null;
		try {
			
			
			User user = (User)session.get(User.class,userId);
			Product product = (Product)session.get(Product.class,productId);
			Criteria criteria = session.createCriteria(SmsBalance.class);
			criteria.add(Restrictions.eq("userId", user.getUserId())).add(Restrictions.eq("productId", product));
			//String sqlQuery = "SELECT balance FROM sms_balance WHERE user_id = :userId and product_id = :productId";
			list = criteria.list();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		finally {
			if(session != null)
			{
				session.close();
			}
		}
		return balance = list.get(0).getBalance();
	}
	@Override
	public int saveUerDeatils(JsonNode jsonNode) {
		
		int temp = 0;
		session =  sessionFactory.openSession();
		tx = session.beginTransaction();
		try {
			
			String password = Global.randomString(6);
			 
			
			//Set<Product> products =new HashSet<>();
			Product product= (Product)session.get(Product.class, jsonNode.get("productId").asInt());	
			//products.add(product);
			
			User user = new User();
			user.setUserName(jsonNode.get("userName").asText());
			user.setPassword(password);
			user.setName(jsonNode.get("name").asText());
			user.setEmail(jsonNode.get("email").asText());
			user.setMobile(jsonNode.get("mobile").asText());
			user.setCity(jsonNode.get("city").asText());
			user.setState(jsonNode.get("state").asText());
			user.setCountry(jsonNode.get("country").asText());
			user.setAddress(jsonNode.get("address").asText());
			user.setRole(jsonNode.get("role").asInt());
			user.setStatus(jsonNode.get("status").asInt());
			user.setCompanyName(jsonNode.get("companyName").asText());
			
			//user.setUserProduct(products);
			
			User resellerUser = (User)session.get(User.class,jsonNode.get("userId").asInt());
			user.setResellerId(resellerUser.getUserId());
			
			UserAuthrization userAuthrization =new UserAuthrization();
			userAuthrization.setDndCheck(jsonNode.get("dndCheck").asText());
			userAuthrization.setSpamCheck(jsonNode.get("spamCheck").asText());
			userAuthrization.setPercentage(jsonNode.get("percentage").asText());
			userAuthrization.setProductId(product.getId());
			userAuthrization.setUserId(user);
			
			//Find Reseller route...........
			Criteria criteria= session.createCriteria(UserProduct.class);
			criteria.add(Restrictions.eq("userId", resellerUser))
					.add(Restrictions.eq("productId", product));
			List<UserProduct>userProductsList = criteria.list();
			
			// save user details in user table
			session.saveOrUpdate(user);
			
			SmsBalance smsBalance = new SmsBalance();
			smsBalance.setBalance(jsonNode.get("balance").asInt());

			smsBalance.setProductId(product);
			
			smsBalance.setUserId(user);
			// save balance in sms_balance  table
			session.saveOrUpdate(smsBalance);
			
			session.saveOrUpdate(userAuthrization);
			//Save user_prodcut 
			
			UserProduct userProduct =new UserProduct();
			userProduct.setProduct(product);
			userProduct.setUserId(user);
			userProduct.setRouteId(userProductsList.get(0).getRouteId());
			
			session.saveOrUpdate(userProduct);
			
			// save data in credit table
			
			Credit credit =new Credit();
			credit.setCredit(jsonNode.get("balance").asInt());
			credit.setCreditBy(resellerUser.getUserId());
			credit.setCurrentAmount(jsonNode.get("balance").asInt());
			credit.setRemark("Credit By "+resellerUser.getUserName()+" And Create New User "+user.getUserName());
			credit.setCreditType(1);
			credit.setPreviousAmouunt(0);
			credit.setProductId(product);
			credit.setUserId(user);
			
			session.saveOrUpdate(credit);
			
			// save reseller balance in debit table
			
			Debit debit = new Debit();
			debit.setDebit(jsonNode.get("balance").asInt());
			debit.setCurrentAmount(jsonNode.get("updateResellerBalance").asInt());
			debit.setPreviousAmouunt(jsonNode.get("previousResellerBalance").asInt());
			debit.setDebitBy(resellerUser.getUserId());
			String debitReasion = "Create New  User, User Name = "+user.getUserName();			
			debit.setRemark(debitReasion);			
			debit.setDebitType(1);			
			debit.setUserId(resellerUser);
			debit.setProductId(product);
			
			session.saveOrUpdate(debit);
			
			// Update Sms_balance table
			String sqlquery = "update sms_balance set balance = "+jsonNode.get("updateResellerBalance").asInt()+" where user_id= "+jsonNode.get("userId").asInt()+" and product_id="+jsonNode.get("productId").asInt();
			Query query = session.createSQLQuery(sqlquery);
			int updateSmsBalanceResult = query.executeUpdate();
			if(updateSmsBalanceResult > 0)
			{
				temp = 1;
				//-------------------------email-----------------------
				String subject = "login credentials";
				String msgBody = "Dear "+user.getName()+"\n\n"
								  + "Plase find login credentials as given below:\n\n"
								  + "User Name: "+user.getUserName()+"\n"
								  + "Password:"+user.getPassword()+"\n\n"
								  + "Thank You\n";
				
				Map<String, String> emailDetails = new HashMap<String, String>();
				emailDetails.put("toAddress", user.getEmail());
				emailDetails.put("subject", subject);
				emailDetails.put("msgBody", msgBody);
				
				emailApi.sendSimpleMail(emailDetails);
				
				
				String subject_support = "Create New User by User Name:-- "+resellerUser.getUserName()+" and new user login credentials";
				String msgBody_support = "Dear "+user.getName()+"\n\n"
								  + "Plase find login credentials as given below:\n\n"
								  + "User Name: "+user.getUserName()+"\n"
								  + "Password:"+user.getPassword()+"\n\n"
								  + "Thank You\n";
				
				Map<String, String> emailDetails_support = new HashMap<String, String>();
				emailDetails_support.put("toAddress", user.getEmail());
				emailDetails_support.put("subject", subject_support);
				emailDetails_support.put("msgBody", msgBody_support);
				
				emailApi.sendSimpleMail(emailDetails_support);

				tx.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
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
	@SuppressWarnings("unchecked")
	@Override
	public List<SmsBalance> getBalanceByUserId(int userId) {
		session = sessionFactory.openSession();
		List<SmsBalance> results = null;
		try {
					
			User user=(User)session.get(User.class,userId);
			Criteria criteria=session.createCriteria(SmsBalance.class);
			criteria.add(Restrictions.eq("userId", user));
			results = criteria.list();
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
		return results;
	}
	@Override
	public List<Credit> getCreditDetailsByUserId(int userId) {
		Session session = sessionFactory.openSession();
		List results = null;
		try {
			String sql = "SELECT * FROM credit WHERE credit_by = :userId";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Credit.class);
			query.setParameter("userId", userId);
			results = query.list();
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
		return results;
	}
	@Override
	public List<Debit> getDebitByUserId(int userId) {
		Session session = sessionFactory.openSession();
		List results = null;
		try {
			String sql = "SELECT * FROM debit WHERE user_id = :userId";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Debit.class);
			query.setParameter("userId", userId);
			results = query.list();
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
		return results;
	}
	@Override
	public Map<Integer,List<User>> getUserByResellerId(int resellerId,int start,int max) {
		session = sessionFactory.openSession();
		List<User> list = null;
		List<User> listCount = null;
		Map<Integer,List<User>> mapResult = new HashMap<>();
		try {
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("resellerId", resellerId))
					.add(Restrictions.eq("status", 1))
					.setFirstResult(start)
					.setMaxResults(max)
					.addOrder(Order.desc("id"));
			list = criteria.list();
			
			Criteria criteriaCount = session.createCriteria(User.class);
			criteriaCount.add(Restrictions.eq("resellerId", resellerId))
					.add(Restrictions.eq("status", 1))
					.setProjection(Projections.rowCount());
			
			listCount = criteriaCount.list();
			mapResult.put(0,list);
			mapResult.put(1,listCount);
			System.out.println("List count :----"+listCount.get(0));
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
		return mapResult;
	}
	@Override
	public int addCreditUser(int creditUserId, int creditByUserId, int productId,int balance) {
		session=sessionFactory.openSession();
		tx = session.beginTransaction();
		User user = (User)session.get(User.class, creditUserId);
		int temp = 0;
		if(user.getUserId() != 0)
		{
			try {
				String remark ="Add Balance By self "+" User name is "+user.getUserName();
				smsHelperService.creditBalance(user.getUserId(), productId, creditByUserId, balance, remark, 3, session, tx);
				
				temp = smsHelperService.debitBalnce(creditUserId, productId,creditByUserId , balance, remark, 3, session, tx);
				
				if(temp == 1)
				{
					tx.commit();
					//session.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				temp = 0;
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
		}
		else
		{
			temp = 2;
		}
		
		return temp;
	}
	@Override
	public int addProdcut(int reselerId, int userId, int ProductId,int balance) {
		
		session=sessionFactory.openSession();
		tx = session.beginTransaction();
		User user = (User)session.get(User.class, userId);
		User reseller = (User)session.get(User.class,reselerId);
		Product product=(Product)session.get(Product.class, ProductId);
		
		int temp=0;
		try {
			
			List<Integer> resellerBalanceList = smsHelperService.getBalance(reseller.getUserId(),product.getId());
			if(resellerBalanceList.size() > 0)
			{
				// save data in credit table
				int resellerBalnce = resellerBalanceList.get(0);
				if(resellerBalnce > balance)
				{
					UserProduct userProduct=new UserProduct();
					userProduct.setProduct(product);
					userProduct.setUserId(user);
					
					session.save(userProduct);
					
					int updateResellerBalance = resellerBalnce - balance;
					SmsBalance smsBalance = new SmsBalance();
					smsBalance.setBalance(balance);
					smsBalance.setProductId(product);	
					smsBalance.setUserId(user);
					// save balance in sms_balance  table
					session.save(smsBalance);
					
					Credit credit =new Credit();
					credit.setCredit(balance);
					credit.setCreditBy(reseller.getUserId());
					credit.setCurrentAmount(balance);
					credit.setRemark("Credit By "+reseller.getUserName()+" And New Product "+user.getUserName());
					credit.setCreditType(3);
					credit.setPreviousAmouunt(0);
					credit.setProductId(product);
					credit.setUserId(user);				
					session.save(credit);
					
					// save reseller balance in debit table
					
					Debit debit = new Debit();
					debit.setDebit(balance);
					debit.setCurrentAmount(updateResellerBalance);
					debit.setPreviousAmouunt(resellerBalnce);
					debit.setDebitBy(reseller.getUserId());
					String debitReasion = "Add New Product, User Name = "+user.getUserName();			
					debit.setRemark(debitReasion);			
					debit.setDebitType(3);			
					debit.setUserId(reseller);
					debit.setProductId(product);					
					session.save(debit);
					
					int result = smsHelperService.updateUserBalance(updateResellerBalance, reseller.getUserId(), product.getId(), session, tx);
					if(result ==1)
					{
						temp = 1;
						tx.commit();
						String subject = "Add New Produt  by User Name:-- "+reseller.getUserName()+"";
						String msgBody = "Dear User "+user.getName()+" Add product Successfully Please chek your make my sms account \n\n"
										 +"Product name Is:---"+product.getName()+"\n\n"
										  + "User Name: "+user.getUserName()+"\n"
										  
										  + "Thank You\n";
						
						Map<String, String> emailDetails = new HashMap<String, String>();
						emailDetails.put("toAddress", user.getEmail());
						emailDetails.put("subject", subject);
						emailDetails.put("msgBody", msgBody);
						
						emailApi.sendSimpleMail(emailDetails);
						
						String subject_support = "Add New Produt  by User Name:-- "+reseller.getUserName()+"";
						String msgBody_support = "Add product Successfully  User:- "+user.getName()+"  \n"
								 			+"Product name Is:---"+product.getName()+"\n\n"
										  + "User Name: "+user.getUserName()+"\n"
										  + "Thank You\n";
						
						Map<String, String> emailDetails_support = new HashMap<String, String>();
						emailDetails_support.put("toAddress", supportEmail);
						emailDetails_support.put("subject", subject_support);
						emailDetails_support.put("msgBody", msgBody_support);
						
						emailApi.sendSimpleMail(emailDetails_support);

						//session.close();
					}
				}
				else
				{
					temp=3;
				}
				
			}
			else
			{
				temp=2;
			}
		}catch (Exception e) {
			e.printStackTrace();
			temp = 0;
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
	public int deductCreditUser(int deductUserId, int dedcutByUserId, int productId, int balance) {
		
		session=sessionFactory.openSession();
		tx = session.beginTransaction();
		User user = (User)session.get(User.class, dedcutByUserId);
		int temp = 0;
		if(user.getUserId() != 0)
		{
			try {
				String remark ="Add Balance By self "+" User name is "+user.getUserName();
				smsHelperService.creditBalance(user.getUserId(), productId, deductUserId, balance, remark, 3, session, tx);				
				temp = smsHelperService.debitBalnce(dedcutByUserId, productId,deductUserId , balance, remark, 3, session, tx);
				if(temp == 1)
				{
					tx.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
				temp = 0;
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
		}
		else
		{
			temp = 2;
		}		
		return temp;
	}
	@Override
	public int changePassword(String oldPassword, String newPassword,int userId) {
		session = sessionFactory.openSession();
		@SuppressWarnings("unused")
		Transaction tx= session.beginTransaction();
		int i=0;
		try {
			User user = (User)session.get(User.class, userId);
			
			if(user.getPassword().equals(oldPassword))
			{
				String sqlquery = "update user set password = "+newPassword+" where id= "+userId;
				Query query = session.createSQLQuery(sqlquery);
				int updatePasswordData = query.executeUpdate();
				
				System.out.println(updatePasswordData);
				if(updatePasswordData > 0)
				{
					tx.commit();
					try {
						session.refresh(user);
						User user2 = (User)session.get(User.class,userId);
						String mobile =user2.getMobile();
						if(mobile.length() == 12)
						{
							mobile = mobile.substring(2);
						}
						String message ="Dear Sir, Your new password is "+user2.getPassword();
						i = Global.sendMessage(userName, password,mobile, senderId, message);
						
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					}
				}			
			}
			else
			{
				//old password is not match
				i=2;
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
		return i;
	}

	@Override
	public List<User> validateUserName(String userName) {
		session = sessionFactory.openSession();
		
		List<User> userList = null;
		try {
			@SuppressWarnings("unused")
			
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("userName", userName));
			userList = criteria.list();
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
		return userList;
	}
	@SuppressWarnings({ "unused", "unchecked", "null", "deprecation"})
	@Override
	public Map<Integer,List> countTransactionList(int userId, int type, int productType) {
		session = sessionFactory.openSession();
		long count;
		Map<Integer, List> mapList = new HashMap<Integer, List>();
		try {
			User user = (User)session.get(User.class,userId);
			Product product = (Product)session.get(Product.class,productType);
			List countlist = null;
			if(user.getUserId() != 0)
			{
				if(product.getId() != 0)
				{
					
					if(type == 1){
						Criteria criteria = session.createCriteria(Credit.class)
								.add(Restrictions.eq("userId",user))
								.add(Restrictions.eq("productId",product))
								.setProjection(Projections.rowCount());
						countlist = criteria.list();
						System.out.println(countlist.size());
						mapList.put(0,countlist);
					}else if(type == 2){
						Criteria criteria = session.createCriteria(Debit.class)
								.add(Restrictions.eq("userId",user))
								.add(Restrictions.eq("productId",product))

								.setProjection(Projections.rowCount());
					
						countlist = criteria.list();
						
						mapList.put(0,countlist);
					 					
					}
				}
				else
				{
					countlist.add("noProductId");
					mapList.put(0,countlist);
				}
			}
			else
			{
				countlist.add("noUserId");
				mapList.put(0,countlist);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapList;
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "null", "deprecation"})
	@Override
	public List  transactionList(int userId, int type, int productType, int start,int limt) {
		session = sessionFactory.openSession();
		List transactionList = null;
		Map<Integer,List> mapList = new HashMap<Integer, List>();
		try {
			User user = (User)session.get(User.class,userId);
			Product product =(Product)session.get(Product.class, productType);
			if(type == 1){
				Criteria criteria = session.createCriteria(Credit.class)
						.add(Restrictions.eq("userId",user))
						.add(Restrictions.eq("productId",product))
						.setFirstResult(start)
						.setMaxResults(limt)
						.addOrder(Order.desc("id"));
				transactionList = criteria.list();
				mapList.put(0,transactionList);
			}else if(type == 2){
				Criteria criteria = session.createCriteria(Debit.class)
						.add(Restrictions.eq("userId",user))
						.add(Restrictions.eq("productId",product))
						.setFirstResult(start)
						.setMaxResults(limt)
						.addOrder(Order.desc("id"));					
				transactionList = criteria.list();
				mapList.put(0,transactionList);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return transactionList;
	}


}
