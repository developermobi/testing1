package com.mobisoft.sms.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;


import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;




@Repository("userReportDao")
public class UserReportDaoImpl implements UserReportDao {

	@Autowired
	SessionFactory sessionFactory;
	
	Session session = null;
	Transaction tx = null;
	
	
	@Override
	public List todayCountMessage(int userId) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate));
		session = sessionFactory.openSession();		
		String sql = "SELECT sum(count) ,status  FROM dlr_status WHERE user_id = "+userId+" and 	logged_at LIKE '%"+localDate+"%' group by status";
		Query query = session.createSQLQuery(sql);

		List results = query.list();
		
		return results;

	}


	@Override
	public List weeklyCountMessage(int userId) {
		session = sessionFactory.openSession();		
		String sql = "SELECT sum(count),status FROM dlr_status WHERE `logged_at` > DATE_SUB(NOW(), INTERVAL 1 WEEK) AND user_id ="+userId+" GROUP BY status";
		Query query = session.createSQLQuery(sql);

		List results = query.list();
		
		return results;
	}


	@Override
	public List monthlyCountMessage(int userId) {
		session = sessionFactory.openSession();		
		String sql = "SELECT sum(count),status FROM dlr_status WHERE `logged_at` > DATE_SUB(NOW(), INTERVAL 2 MONTH) AND user_id ="+userId+" GROUP BY status";
		Query query = session.createSQLQuery(sql);
		List results = query.list();
		
		return results;
	}
	
}
