package com.mobisoft.sms.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.SmsBalance;

@Repository("smsHelperDao")
public class SmsHelperDaoImpl implements SmsHelperDao{
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Integer> getBalance(int userId, int productId) {
		
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sqlQuery = "select balance from sms_balance where user_id = "+userId+" and product_id ="+productId;
		Query query = session.createSQLQuery(sqlQuery);
		List list = query.list();
		return list;
	}

}
