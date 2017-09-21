package com.mobisoft.sms.dao;

import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.service.DlrStatusService;
import com.mobisoft.sms.service.SmsHelperService;

@Repository("userJobsDao")
public class UserJobsDaoImpl implements UserJobsDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	SmsHelperService smsHelperService;

	@Autowired
	DlrStatusService dlrStatusService;
	
	 Session session = null;
	 Transaction tx = null;
	
	@Override
	public int saveUserJobs(UserJobs userJobs,int productId,int sentMessageBalance, int updateNewBalance) {
		int flag = 0;
		try {
			session = sessionFactory.openSession();  			
			tx = session.beginTransaction();			
					
			session.save(userJobs);
			int temp = smsHelperService.debitBalnce(userJobs.getUserId(), productId,userJobs.getUserId() , sentMessageBalance, "Sent Message By Self", 4, session, tx);
			System.out.println("Return update balnmce"+temp);
			if(temp == 1)
			{
				tx.commit();			
				flag = 1;
			}
					  
		}catch (Exception ex) {
			tx.rollback();
			System.out.println(ex.getMessage());
		}finally {		
			session.close();			
		}  
	return flag;
	}

	@Override
	public int sendQuickMessage(Map<String, Object> mapList) {
		int flag = 0;
		try {
			session = sessionFactory.openSession(); 
			tx = session.beginTransaction();
			int userId= (int)mapList.get("userId");			
			int productId =(int) mapList.get("productId");
			int sentMessage = (int)mapList.get("sentMessage");			
			flag = smsHelperService.debitBalnce(userId, productId,userId, sentMessage, "Sent Message By Self using quick", 4, session, tx);			
			if(flag ==1)
			{
				int result = dlrStatusService.saveQuickMessage(mapList,session);
				if(result == 1)
				{	
					tx.commit();
					flag = 1;
				}				
			}	  
		}catch (Exception ex) {
			tx.rollback();
			System.out.println(ex.getMessage());
		}finally {		
			session.close();			
		}  
	return flag;
	}	
}
