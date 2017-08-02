package com.mobisoft.sms.dao;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.Product;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;

import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.SmsHelper;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	SessionFactory sessionFactory;
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
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("id", userId)).add(Restrictions.eq("status", 1));		
		List<User> list = criteria.list();
		session.close();
		return list;
	}
	@Override
	public int updateUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			
			String sql = "UPDATE User SET user_name = :userName,address = :address,city = :city,company_name = :companyName,country = :country,email = :email,mobile = :mobile, name = :name,role = :role,state = :state, status = :status WHERE id = :id";
			
			Query qry = session.createQuery(sql);
			qry.setParameter("userName", user.getUserName());
			qry.setParameter("address", user.getAddress());
			qry.setParameter("city", user.getCity());
			qry.setParameter("companyName", user.getCompanyName());
			qry.setParameter("country", user.getCountry());
			qry.setParameter("email", user.getEmail());
			qry.setParameter("mobile", user.getMobile());
			qry.setParameter("name", user.getName());
			qry.setParameter("role", user.getRole());
			qry.setParameter("state", user.getState());
			qry.setParameter("status", user.getStatus());
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
	public int deleteUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			
			String sql = "UPDATE User set status = :status WHERE id = :userId";
			
			Query qry = session.createQuery(sql);
			qry.setParameter("status", user.getStatus());
			qry.setParameter("userId", user.getUserId());		
			
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
	public List<User> getUserByUserName(String userName) {
		
		//System.out.println("sdjhsakdjkjasldsad  "+userName);
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
		/*Query query = session.createQuery(sqlQuery);
		query.setParameter("userId", userId);
		query.setParameter("productId",productId);
		List results = query.list();
		System.out.println(results.get(0));
		balance = (int) results.get(0);*/
		return balance;
	}
	@Override
	public int saveUerDeatils(JsonNode jsonNode) {
		System.out.println("as;ldlasdlajksdasdjsadlksajdlkasdsajd");
		System.out.println(jsonNode.get("productId"));
		String password = Global.randomString(6);
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
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
		
		Set<Product> products =new HashSet<>();
		Product product= new Product();
		product.setId(jsonNode.get("productId").asInt());
		products.add(product);
		
		System.out.println(product.getId()+"product name:--"+product.getName());
		user.setUserProduct(products);
		
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
			

			
			// save data in credit table
			
			Credit credit =new Credit();
			credit.setCredit(jsonNode.get("balance").asInt());
			credit.setCreditBy(jsonNode.get("creditBy").asText());
			credit.setCurrentAmount(jsonNode.get("balance").asInt());
			credit.setPreviousAmouunt(0);
			credit.setUserId(user);
			
			session.saveOrUpdate(credit);
			
			// save reseller balance in debit table
			
			Debit debit = new Debit();
			debit.setDebit(jsonNode.get("balance").asInt());
			debit.setCurrentAmount(jsonNode.get("updateResellerBalance").asInt());
			debit.setPreviousAmouunt(jsonNode.get("previousResellerBalance").asInt());
			String debitReasion = "Create New  User, User Name = "+user.getUserName();
			debit.setDebitBy(debitReasion);
			User resellerUser = new User();
			resellerUser.setUserId(jsonNode.get("userId").asInt());
			debit.setUserId(resellerUser);
			
			session.saveOrUpdate(debit);
			
			// Update Sms_balance table
			String sqlquery = "update sms_balance set balance = "+jsonNode.get("updateResellerBalance").asInt()+" where user_id= "+jsonNode.get("userId").asInt()+" and product_id="+jsonNode.get("productId").asInt();
			Query query = session.createSQLQuery(sqlquery);
			int updateSmsBalanceResult = query.executeUpdate();
			if(updateSmsBalanceResult > 0)
			{
				
			}
			
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

}
