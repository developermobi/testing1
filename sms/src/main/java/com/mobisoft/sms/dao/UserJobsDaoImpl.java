package com.mobisoft.sms.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.UserJobs;

@Repository("userJobsDao")
public class UserJobsDaoImpl implements UserJobsDao {

	@Autowired
	SessionFactory sessionFactory;

	 Session session = null;
	 Transaction tx = null;
	
	@Override
	public int saveUserJobs(UserJobs userJobs) {
		int flag = 0;
		try {
			session = sessionFactory.openSession();  			
			tx = session.getTransaction();			
			tx.begin();			
			session.save(userJobs);			  
			tx.commit();			
			flag = 1;			  
		}catch (Exception ex) {
			tx.rollback();			
		}finally {		
			session.close();			
		}  
	return flag;
	}
}
