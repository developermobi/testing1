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
	private SessionFactory sessionFactory;
	
	@Autowired
	private SmsHelperService smsHelperService;

	@Autowired
	private DlrStatusService dlrStatusService;
	
	 private Session session = null;
	 private Transaction tx = null;
	
	@Override
	public int saveUserJobs(UserJobs userJobs,int productId,int sentMessageBalance, int updateNewBalance) {
		int flag = 0;
		try {
			final Session session = sessionFactory.openSession();
			final Transaction tx = session.beginTransaction();	
			String remark = "Message sent by self by";
			switch (userJobs.getJobType()) {
			case 1:
				remark +=" quick method";
				break;
			case 2:
				remark +=" group";
				break;
			case 3:
				remark +=" notepad file";
				break;
			case 4:
				remark +=" csv file";
				break;
			default:
				remark +=" Personalize message";
				break;
			}
			
			session.save(userJobs);
			int temp = smsHelperService.debitBalnce(userJobs.getUserId(), productId,userJobs.getUserId() , sentMessageBalance, remark, 4, session, tx);
			System.out.println("Return update balnmce"+temp);
			if(temp == 1)
			{
				tx.commit();
				//session.close();
				flag = 1;
			}
					  
		}catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
		}finally {		
			try {
				if(session != null)
				{
					session.close();	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}  
	return flag;
	}

	@Override
	public int sendQuickMessage(Map<String, Object> mapList) {
		int flag = 0;
		try {
			final Session session = sessionFactory.openSession(); 
			final Transaction tx = session.beginTransaction();
			int userId= (int)mapList.get("userId");			
			int productId =(int) mapList.get("productId");
			int sentMessage = (int)mapList.get("sentMessage");			
			flag = smsHelperService.debitBalnce(userId, productId,userId, sentMessage, "Sent Message By Self using quick", 4, session, tx);			
			if(flag ==1)
			{
				int result = dlrStatusService.saveQuickMessage(mapList,session);
				if(result == 1)
				{	
					flag = 1;
				}				
			}	  
		}catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
		}finally {		
			try {
				if(session != null)
				{
					session.close();	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}  
	return flag;
	}	
}
