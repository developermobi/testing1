package com.mobisoft.sms.dao;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.jsf.FacesContextUtils;

import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.Product;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserProduct;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.SmsHelper;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private SmsHelperService smsHelperService;
	public int saveUser(User user) {
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			session.saveOrUpdate(user);
			temp = 1;
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
			tx.rollback();
		}finally {
			session.close();
		}
		return temp;
	}
	@Override
	public List<User> getUser() {
		Session session = sessionFactory.openSession();		
		List<User> list = session.createCriteria(User.class).list();
		session.close();
		return list;
	}
	@Override
	public List<User> getUserById(int userId) {

		Session session = sessionFactory.openSession();	
		String sql = "SELECT * FROM user_product WHERE user_id = :userId";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(UserProduct.class);
		query.setParameter("userId", userId);
		List results = query.list();
		return results;
	}
	@Override
	public int updateUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			
			//String sql = "UPDATE User SET user_name = :userName,address = :address,city = :city,company_name = :companyName,country = :country,email = :email,mobile = :mobile, name = :name,role = :role,state = :state, status = :status WHERE id = :id";
			String sql = "UPDATE User SET address = :address,city = :city,company_name = :companyName,country = :country,email = :email,mobile = :mobile, name = :name,state = :state WHERE id = :id";
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
			qry.setParameter("id", user.getUserId());
			
			temp = qry.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
			tx.rollback();
		}finally {
			session.close();
		}
		
		return temp;
	}
	@Override
	public int deleteUser(int userId,int resellerId) {
		int temp = 0;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		User user=(User)session.get(User.class, userId);
		if(user.getStatus() == 1)
		{
			try {
				String remark = "Delete User By Self "+" User Name Is"+ user.getUserName();
				temp = smsHelperService.deductBalanceDeleteUser(user.getUserId(),resellerId,  remark, 2,session,tx);
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
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				temp = 2;
				tx.rollback();
				
			}finally {
				session.close();
			}			
		}
		else
		{
			temp =2;
		}
		return temp;
		
	}
	@Override
	public List<User> getUserByUserName(String userName) {

		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("userName", userName));
		List<User> list = criteria.list();
		session.close();
		return list;
	}
	public int getBalnce(int userId,int productId)
	{
		int balance = 0;
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sqlQuery = "SELECT balance FROM sms_balance WHERE user_id = :userId and product_id = :productId";
		System.out.println(sqlQuery);
		return balance;
	}
	@Override
	public int saveUerDeatils(JsonNode jsonNode) {

		String password = Global.randomString(6);
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
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
		userAuthrization.setUserId(user);
		
		//Find Reseller route...........
		Criteria criteria= session.createCriteria(UserProduct.class);
		criteria.add(Restrictions.eq("userId", resellerUser))
				.add(Restrictions.eq("productId", product));
		List<UserProduct>userProductsList = criteria.list();
		
		
		
		int temp = 0;
		
		try {
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
				tx.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
			tx.rollback();
		}finally {
			session.close();
		}
		return temp;
	}
	@Override
	public List<SmsBalance> getBalanceByUserId(int userId) {
		Session session = sessionFactory.openSession();		
		User user=(User)session.get(User.class,userId);
		Criteria criteria=session.createCriteria(SmsBalance.class);
		criteria.add(Restrictions.eq("userId", user));
		List<SmsBalance> results = criteria.list();
		return results;
	}
	@Override
	public List<Credit> getCreditDetailsByUserId(int userId) {
		Session session = sessionFactory.openSession();	
		String sql = "SELECT * FROM credit WHERE credit_by = :userId";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Credit.class);
		query.setParameter("userId", userId);
		List results = query.list();
		return results;
	}
	@Override
	public List<Debit> getDebitByUserId(int userId) {
		Session session = sessionFactory.openSession();	
		String sql = "SELECT * FROM debit WHERE user_id = :userId";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Debit.class);
		query.setParameter("userId", userId);
		List results = query.list();
		return results;
	}
	@Override
	public List<User> getUserByResellerId(int resellerId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("resellerId", resellerId)).add(Restrictions.eq("status", 1));
		List<User> list = criteria.list();
		session.close();
		return list;
	}
	@Override
	public int addCreditUser(int creditUserId, int creditByUserId, int productId,int balance) {
		Session session=sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
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
				}
			} catch (Exception e) {
				e.printStackTrace();
				temp = 0;
				tx.rollback();
			}finally {
				session.close();
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
		
		Session session=sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
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
			session.close();
		}
		return temp;

	}
	@Override
	public int deductCreditUser(int deductUserId, int dedcutByUserId, int productId, int balance) {
		
		Session session=sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
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
				session.close();
			}
		}
		else
		{
			temp = 2;
		}
		
		return temp;
	}


}
