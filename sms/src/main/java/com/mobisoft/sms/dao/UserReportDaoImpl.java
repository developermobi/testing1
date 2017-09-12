package com.mobisoft.sms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

@Repository("userReportDao")
public class UserReportDaoImpl implements UserReportDao {

	@Autowired
	SessionFactory sessionFactory;
	
	Session session = null;
	Transaction tx = null;
	
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
	@Override
	public List weeklyCountMessage(int userId) {
		session = sessionFactory.openSession();		
		String sql = "SELECT sum(count),status FROM dlr_status WHERE `logged_at` > DATE_SUB(NOW(), INTERVAL 1 WEEK) AND user_id ="+userId+" GROUP BY status";
		Query query = session.createSQLQuery(sql);
		List results = query.list();		
		return results;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List monthlyCountMessage(int userId) {
		session = sessionFactory.openSession();		
		String sql = "SELECT sum(count),status FROM dlr_status WHERE `logged_at` > DATE_SUB(NOW(), INTERVAL 2 MONTH) AND user_id ="+userId+" GROUP BY status";
		Query query = session.createSQLQuery(sql);
		List results = query.list();		
		return results;
	}

	@Override
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max) {
		
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(DlrStatus.class);
		criteria.add(Restrictions.eq("userId",userId)).setFirstResult(start).setMaxResults(max);
		@SuppressWarnings("unchecked")
		List<DlrStatus> listResult = criteria.list();	
		return listResult;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List archiveReportByUserId(final int userId,final String startDate,final String endDate) {
		System.out.println("in");
		session = sessionFactory.openSession();
		final List list = new ArrayList<String>();
		session.doWork(new Work() {			   
		       @Override
		       public void execute(Connection conn) throws SQLException {
		         Statement pstmtDlrStatus = conn.createStatement();
		         System.out.println("in conn");
		          try{
		           String sqlInsertDlrStatus = "SELECT mobile, Sender, message, STATUS, logged_at, dlr_time FROM dlr_status WHERE logged_at BETWEEN '"+startDate+"' AND '"+endDate+"' AND user_id="+userId+"";
		           System.out.println(sqlInsertDlrStatus);
		           ResultSet rs = pstmtDlrStatus.executeQuery(sqlInsertDlrStatus);
		           while(rs.next())
		           {
		        	   
		        	   String message = rs.getString("message");
		        	   message = message.replaceAll(",", "");
		        	   list.add(rs.getString("mobile"));
		        	   list.add(rs.getString("Sender"));
		        	   list.add(message);
		        	   list.add(rs.getString("STATUS"));
		        	   list.add(rs.getString("logged_at"));
		        	   list.add(rs.getString("dlr_time"));
       	   
		           }
		           conn.setAutoCommit(false);
		           conn.commit();
		           conn.setAutoCommit(true);
		           
		         } 
		         finally{
		        	 pstmtDlrStatus .close();
		        	 conn.close();
		         }                                
		     }
		});
		return list;
		
	}
	@Override
	public List<UserJobs> scheduleReportByUserId(int userId,int start,int max) {
		session = sessionFactory.openSession();
		tx= session.beginTransaction();
		
		Criteria criteria = session.createCriteria(UserJobs.class);
		criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("scheduleStatus", 1)).setFirstResult(start).setMaxResults(max);
		List<UserJobs> listResult = criteria.list();	
		return listResult;
		
	}
	
}