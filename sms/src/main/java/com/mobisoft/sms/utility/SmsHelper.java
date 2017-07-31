package com.mobisoft.sms.utility;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public class SmsHelper {

	@Autowired
	SessionFactory sessionFactory;
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
	/*public static void main(String[] args) {
		
		SmsHelper smsHelper = new SmsHelper();
		int balance = smsHelper.getBalnce(1,1);
		System.out.println(balance);
	}*/
}
