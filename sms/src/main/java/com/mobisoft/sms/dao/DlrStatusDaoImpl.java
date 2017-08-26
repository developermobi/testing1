package com.mobisoft.sms.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.utility.Global;
import com.mysql.jdbc.PreparedStatement;

@Repository("dlrStatusDao")
public class DlrStatusDaoImpl implements DlrStatusDao{

	
	@Autowired
	SessionFactory sessionFactory;
	
	@Value("${uploadUserTextFile}")
	private String uploadUserTextFile;
	
	Session session = null;
	Transaction tx = null;
	File file;
	FileReader fr = null;
	BufferedReader br = null;
	
	private int temp=0;
	
	@Override
	public int saveDlrStatus(DlrStatus dlrStatus) {
		
		
		session = sessionFactory.openSession();
		tx= session.beginTransaction();
		final Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserJobs.class);
		criteria.add(Restrictions.eq("jobStatus", 0));
		final List<UserJobs> list = criteria.list();
		System.out.println("List Size data"+list.size());
		
		if(list.size() > 0)
		{
			
			try {
				
				String sql = "UPDATE user_jobs set job_status = :status WHERE user_id = :userId and id = :id";
				org.hibernate.Query qry = session.createSQLQuery(sql);
				qry.setParameter("status", 1);
				qry.setParameter("userId", list.get(0).getUserId());
				qry.setParameter("id", list.get(0).getId());
				int updateJobStatus =qry.executeUpdate();
				
				if(updateJobStatus ==1)
				{
					String fileName = list.get(0).getFilename();
					file = new File(uploadUserTextFile,fileName);					
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					String line="";
					final List<String> mobileList = new ArrayList<>();
					while((line = br.readLine()) != null)
					{
						mobileList.add(line);
					}
					/*for (int i = 0; i < mobileList.size(); i++) {
						org.hibernate.Query query = session.createSQLQuery("CALL spInsert(:job_id,:momt,:Sender,:mobile,:message,:provider_id,:user_id,:type,:mclass,:count)")
								  .addEntity(DlrStatus.class)
								  .setParameter("job_id",list.get(0).getId())
								  .setParameter("momt", "MO")
								  .setParameter("Sender",list.get(0).getSender())
								  .setParameter("mobile",mobileList.get(0))
								  .setParameter("message",list.get(0).getMessage())
								  .setParameter("count", list.get(0).getCount())
								  .setParameter("provider_id","New")
								  .setParameter("user_id",list.get(0).getUserId())
								  .setParameter("type", 1)
								  .setParameter("mclass", 1);
						query.executeUpdate();
						

					}*/	
					//get Connction from Session
					session.doWork(new Work() {
						   
					       @Override
					       public void execute(Connection conn) throws SQLException {
					          PreparedStatement pstmtDlrStatus = null;
					          PreparedStatement pstmtQueuedSms = null;
					          try{
					           String sqlInsertDlrStatus = "INSERT INTO dlr_status(job_id,Sender, coding, count,length, message, message_id, mobi_class, mobile, provider_id, type, user_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
					           pstmtDlrStatus = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus );
					           
					           String sqlInsertQueued ="INSERT INTO queued_sms(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service,mclass, coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					           pstmtQueuedSms = (PreparedStatement)conn.prepareStatement(sqlInsertQueued);
					           int i=0;
					           for(String mobile : mobileList){	        	   

					        	   String messId = Global.randomString(10);			        	   
					        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
					        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
					        	   pstmtDlrStatus.setInt(3,0);
					        	   pstmtDlrStatus.setInt(4, list.get(0).getCount());
					        	   pstmtDlrStatus.setInt(5, list.get(0).getMessageLength());
					        	   pstmtDlrStatus.setString(6, list.get(0).getMessage());
					        	   pstmtDlrStatus.setString(7, messId);
					        	   pstmtDlrStatus.setInt(8, 1);
					        	   pstmtDlrStatus.setString(9, mobile);
					        	   pstmtDlrStatus.setString(10, list.get(0).getRoute());
					        	   pstmtDlrStatus.setInt(11, 1);
					        	   pstmtDlrStatus.setInt(12, list.get(0).getUserId());
					               pstmtDlrStatus.addBatch();
					               
					               pstmtQueuedSms.setInt(1, list.get(0).getId());
					               pstmtQueuedSms.setString(2,"MO");
					               pstmtQueuedSms.setString(3,list.get(0).getSender());
					               pstmtQueuedSms.setString(4, mobile);
					               pstmtQueuedSms.setString(5, list.get(0).getMessage());
					               pstmtQueuedSms.setString(6,list.get(0).getRoute());
					               pstmtQueuedSms.setString(7,"sqlbox");
					               pstmtQueuedSms.setInt(8, 1);
					               pstmtQueuedSms.setInt(9, 1);
					               pstmtQueuedSms.setInt(10, 0);
					               pstmtQueuedSms.setInt(11,19);
					               pstmtQueuedSms.setString(12, messId);
					               pstmtQueuedSms.setString(13,"UTF-8");
					               pstmtQueuedSms.addBatch(); 

					           }
					           conn.setAutoCommit(false);
					           pstmtDlrStatus.executeBatch();
					           pstmtQueuedSms.executeBatch();
					           String sql = "UPDATE user_jobs set job_status = :status WHERE user_id = :userId and id = :id";
					           org.hibernate.Query qry = session.createSQLQuery(sql);
					           qry.setParameter("status", 2);
					           qry.setParameter("userId", list.get(0).getUserId());
					           qry.setParameter("id", list.get(0).getId());			
					           temp =qry.executeUpdate();
					           conn.commit();
					           conn.setAutoCommit(true);
					           
					         } 
					         finally{
					        	 pstmtDlrStatus .close();
					         }                                
					     }

					});
				}
				
					
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				
				tx.rollback();
			}finally{
				session.close();
			}
		}
		else
		{
			return temp;
		}

		return temp;
	}

}
