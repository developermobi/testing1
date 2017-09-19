package com.mobisoft.sms.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.apache.commons.io.FilenameUtils;
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

import com.fasterxml.jackson.databind.JsonNode;
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
	public int saveDlrStatus() {
		
		session = sessionFactory.openSession();
		tx= session.beginTransaction();
		final Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserJobs.class);
		criteria.add(Restrictions.eq("jobStatus", 0)).add(Restrictions.eq("scheduleStatus", 0));
		final List<UserJobs> list = criteria.list();
		System.out.println("List Size data"+list.size());
		
		if(list.size() > 0)
		{
			try {
				session.doWork(new Work() {
					   
				       @Override
				       public void execute(Connection conn) throws SQLException {
				          PreparedStatement pstmtDlrStatus = null;
				          PreparedStatement pstmtQueuedSms = null;
				          try{
								String sql = "UPDATE user_jobs set job_status = :status WHERE user_id = :userId and id = :id";
								org.hibernate.Query qry = session.createSQLQuery(sql);
								qry.setParameter("status", 1);
								qry.setParameter("userId", list.get(0).getUserId());
								qry.setParameter("id", list.get(0).getId());
								int updateJobStatus =qry.executeUpdate();
								final List<String> mobileList = new ArrayList<>();
								if(updateJobStatus ==1)
								{									
								/*try {*/
								String fileName = list.get(0).getFilename();
								/*	String fileExtension = FilenameUtils.getExtension(fileName);
									System.out.println(fileExtension);*/
								file = new File(fileName);					
								fr = new FileReader(file);
								br = new BufferedReader(fr);
								String line="";
										
									while((line = br.readLine()) != null)
									{
										mobileList.add(line);
									}
									if(mobileList.isEmpty())
									{
										 temp=0;
									}
								}
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
					           String sql1 = "UPDATE user_jobs set job_status = :status WHERE user_id = :userId and id = :id";
					           org.hibernate.Query qry1 = session.createSQLQuery(sql1);
					           qry1.setParameter("status", 2);
					           qry1.setParameter("userId", list.get(0).getUserId());
					           qry1.setParameter("id", list.get(0).getId());			
					           temp =qry1.executeUpdate();
					           conn.commit();
					           conn.setAutoCommit(true);
					           
					         } catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								tx.rollback();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								tx.rollback();
							} 
					         finally{
					        	 pstmtDlrStatus .close();
					        	 
					         }                                
				     }

				});
					
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
			
			return temp = 0;
		}

		return temp;
	}


	@Override
	public int saveQuickMessage(final Map<String,Object> mapList,Session session1) {
		String mobileNumber = (String) mapList.get("mobileNumber");
		final List<String> mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
		
		try {
			session1.doWork(new Work() {
				
			       @Override
			       public void execute(Connection conn) throws SQLException {
			    	  
			    	   	  int jobId = (int) (System.currentTimeMillis() & 0xfffffff);	
				    	  String sender = (String)mapList.get("sender");
				    	  
				    	  int coding = (int)mapList.get("coding");
				    	  int messageCount = (int)mapList.get("messageCount");
				    	  int length = (int)mapList.get("messageLength");
				    	  String message = (String)mapList.get("message");
				    	  System.out.println(messageCount);

				    	  int mobi_class = 1;
				    	  String providerId = (String)mapList.get("routeName");
				    	  int type = 1;
				    	  int  userId = (int)mapList.get("userId");
				    	  String momt = "mo";
				    	  String boxcId = "sqlbox";
				    	  int service =1;
				    	  int mClass = 1;
				    	  int dlrMask = 19;
				    	  String chaset = "UTF-8";
				    	  
				          PreparedStatement pstmtDlrStatus = null;
				          PreparedStatement pstmtQueuedSms = null;
				          try{
				           String sqlInsertDlrStatus = "INSERT INTO dlr_status(job_id,Sender, coding, count,length, message, message_id, mobi_class, mobile, provider_id, type, user_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				           pstmtDlrStatus = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus );
				           
				           String sqlInsertQueued ="INSERT INTO queued_sms_quick(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service,mclass, coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				           pstmtQueuedSms = (PreparedStatement)conn.prepareStatement(sqlInsertQueued);
				           int i=0;
				           for(String mobile : mobileList){	        	   

				        	   String messId = Global.randomString(10);    	   
				        	   pstmtDlrStatus.setInt(1, jobId);
				        	   pstmtDlrStatus.setString(2, sender);
				        	   pstmtDlrStatus.setInt(3,coding);
				        	   pstmtDlrStatus.setInt(4, messageCount);
				        	   pstmtDlrStatus.setInt(5, length);
				        	   pstmtDlrStatus.setString(6, message);
				        	   pstmtDlrStatus.setString(7, messId);
				        	   pstmtDlrStatus.setInt(8, mobi_class);
				        	   pstmtDlrStatus.setString(9, mobile);
				        	   pstmtDlrStatus.setString(10,providerId);
				        	   pstmtDlrStatus.setInt(11, type);
				        	   pstmtDlrStatus.setInt(12, userId);
				               pstmtDlrStatus.addBatch();
				               
				               pstmtQueuedSms.setInt(1, jobId);
				               pstmtQueuedSms.setString(2,momt);
				               pstmtQueuedSms.setString(3,sender);
				               pstmtQueuedSms.setString(4, mobile);
				               pstmtQueuedSms.setString(5, message);
				               pstmtQueuedSms.setString(6,providerId);
				               pstmtQueuedSms.setString(7,boxcId);
				               pstmtQueuedSms.setInt(8, service);
				               pstmtQueuedSms.setInt(9, mobi_class);
				               pstmtQueuedSms.setInt(10, coding);
				               pstmtQueuedSms.setInt(11,dlrMask);
				               pstmtQueuedSms.setString(12, messId);
				               pstmtQueuedSms.setString(13,chaset);
				               pstmtQueuedSms.addBatch(); 

				           }
				           conn.setAutoCommit(false);
				           pstmtDlrStatus.executeBatch();
				           pstmtQueuedSms.executeBatch();		          
				           conn.commit();
				           //conn.setAutoCommit(true);
				           temp=1;
				           
				         } 
				         finally{
				        	 pstmtDlrStatus .close();
				        	 pstmtQueuedSms .close();
				         }
			     }

			});
		} catch (Exception e) {
			
		}
		
		return temp;
	}
	

}
