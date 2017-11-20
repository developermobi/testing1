package com.mobisoft.sms.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.utility.Global;
import com.mysql.jdbc.PreparedStatement;

@Repository("dlrStatusDao")
public class DlrStatusDaoImpl implements DlrStatusDao{

	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Value("${uploadUserTextFile}")
	private String uploadUserTextFile;
	
	@Autowired
	private SmsHelperService smsHelperService;
	
	File file;
	FileReader fr = null;
	BufferedReader br = null;
	
	private int temp=0;
	
	@Override
	public int saveDlrStatus(List<UserJobs> list) {
		
		final List<UserAuthrization> listCheckAutherization = smsHelperService.getUserAuthrizationCheck(list.get(0).getUserId(),list.get(0).getProductId());
		
		final Session session = sessionFactory.openSession();
		
		try {
		
			if(list.size() > 0)
			{
				try {
					session.doWork(new Work() {
						   
					       @Override
					       public void execute(Connection conn) throws SQLException {
					          PreparedStatement pstmtDlrStatus = null;
					          PreparedStatement pstmtQueuedSms = null;
					          PreparedStatement pstmtQueuedSms1 = null;
					          PreparedStatement pstmtQueuedSms2 = null;
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
									
								   int code = 0;
								   if(list.get(0).getMessageType() == 1)
								   {
									   code =0;
								   }else if(list.get(0).getMessageType() == 2)
								   {
									   code = 4;
								   }else if(list.get(0).getMessageType() == 3)
								   {
									   code = 2;
								   }
								   
								   int percent = 100;
								   if(list.get(0).getJobType() == 3 || list.get(0).getJobType() == 4 ){
									   if( mobileList.size() >= 5000)
									   {
										   percent = Integer.parseInt(listCheckAutherization.get(0).getPercentage());
										   
										   Collections.shuffle(mobileList);
									   }
									   
								   }	
								 //  System.out.println("percent-"+percent);
								   int mobileListSize = mobileList.size();
									  
								   int percentCount=0;
								   if(percent == 100){
									   percentCount = mobileListSize;	
								   }else{
									   percentCount = (mobileListSize / 100) * percent;	
								   }
								   
								   final List<String> mobileListDelivered = mobileList.subList(0, percentCount);
								   
								   final List<String> mobileListFake = mobileList.subList(percentCount, mobileList.size());
								   
								   System.out.println("mobileListDelivered: "+mobileListDelivered.size());
								   System.out.println("mobileListFake: "+mobileListFake.size());
				   
						           String sqlInsertDlrStatus = "INSERT INTO dlr_status(job_id,Sender, coding, count,length, message, message_id,  mobile, provider_id, type, user_id,mobi_class) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
						           pstmtDlrStatus = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus );
						           
						           String sqlInsertQueued ="INSERT INTO queued_sms(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service, coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
						           pstmtQueuedSms = (PreparedStatement)conn.prepareStatement(sqlInsertQueued);
						           
						           String sqlInsertQueued1 ="INSERT INTO queued_sms2(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service, coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
						           pstmtQueuedSms1 = (PreparedStatement)conn.prepareStatement(sqlInsertQueued1);
						           
						           String sqlInsertQueued2 ="INSERT INTO queued_sms3(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service, coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
						           pstmtQueuedSms2 = (PreparedStatement)conn.prepareStatement(sqlInsertQueued2);
						           int i=0;

						           if(mobileListDelivered.size() > 0 && mobileListDelivered.size() <= 100){
						        	   for(String mobile : mobileListDelivered){
						        			
							        	   String messId = Global.randomString(10);	
							        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
							        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
							        	   pstmtDlrStatus.setInt(3,code);
							        	   pstmtDlrStatus.setInt(4, list.get(0).getCount());
							        	   pstmtDlrStatus.setInt(5, list.get(0).getMessageLength());
							        	   pstmtDlrStatus.setString(6, list.get(0).getMessage());
							        	   pstmtDlrStatus.setString(7, messId);							        	 
							        	   pstmtDlrStatus.setString(8, mobile);
							        	   pstmtDlrStatus.setString(9, list.get(0).getRoute());
							        	   pstmtDlrStatus.setInt(10, 1);
							        	   pstmtDlrStatus.setInt(11, list.get(0).getUserId());
							        	   pstmtDlrStatus.setInt(12, 0);
							               pstmtDlrStatus.addBatch();
							               
							               pstmtQueuedSms.setInt(1, list.get(0).getId());
							               pstmtQueuedSms.setString(2,"MT");
							               pstmtQueuedSms.setString(3,list.get(0).getSender());
							               pstmtQueuedSms.setString(4, mobile);
							               pstmtQueuedSms.setString(5, list.get(0).getMessage());
							               pstmtQueuedSms.setString(6,list.get(0).getRoute());
							               pstmtQueuedSms.setString(7,"sqlbox");
							               pstmtQueuedSms.setInt(8, 1);							             
							               pstmtQueuedSms.setInt(9, code);
							               pstmtQueuedSms.setInt(10,19);
							               pstmtQueuedSms.setString(11, messId);
							               pstmtQueuedSms.setString(12,"UTF-8");
							               pstmtQueuedSms.addBatch(); 
			
							           }
						           }else{
						        	   int queuedSmsCount = mobileListDelivered.size()/3;
						        	   int queuedSms1Count = queuedSmsCount + queuedSmsCount; 
						        	   final List<String> queuedSmsList = mobileListDelivered.subList(0, queuedSmsCount);
									   final List<String> queuedSmsList1= mobileListDelivered.subList(queuedSmsCount,queuedSms1Count);
									   final List<String> queuedSmsList2 = mobileListDelivered.subList(queuedSms1Count, mobileListDelivered.size());
									   if(queuedSmsList.size() > 0){
							        	   for(String mobile : queuedSmsList){
							        			
								        	   String messId = Global.randomString(10);	
								        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
								        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
								        	   pstmtDlrStatus.setInt(3,code);
								        	   pstmtDlrStatus.setInt(4, list.get(0).getCount());
								        	   pstmtDlrStatus.setInt(5, list.get(0).getMessageLength());
								        	   pstmtDlrStatus.setString(6, list.get(0).getMessage());
								        	   pstmtDlrStatus.setString(7, messId);							        	 
								        	   pstmtDlrStatus.setString(8, mobile);
								        	   pstmtDlrStatus.setString(9, list.get(0).getRoute());
								        	   pstmtDlrStatus.setInt(10, 1);
								        	   pstmtDlrStatus.setInt(11, list.get(0).getUserId());
								        	   pstmtDlrStatus.setInt(12, 0);
								               pstmtDlrStatus.addBatch();
								               
								               pstmtQueuedSms.setInt(1, list.get(0).getId());
								               pstmtQueuedSms.setString(2,"MT");
								               pstmtQueuedSms.setString(3,list.get(0).getSender());
								               pstmtQueuedSms.setString(4, mobile);
								               pstmtQueuedSms.setString(5, list.get(0).getMessage());
								               pstmtQueuedSms.setString(6,list.get(0).getRoute());
								               pstmtQueuedSms.setString(7,"sqlbox");
								               pstmtQueuedSms.setInt(8, 1);							             
								               pstmtQueuedSms.setInt(9, code);
								               pstmtQueuedSms.setInt(10,19);
								               pstmtQueuedSms.setString(11, messId);
								               pstmtQueuedSms.setString(12,"UTF-8");
								               pstmtQueuedSms.addBatch(); 
				
								           }
							        	   
							           }
									   if(queuedSmsList1.size() > 0){
							        	   for(String mobile : queuedSmsList1){
							        			
								        	   String messId = Global.randomString(10);	
								        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
								        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
								        	   pstmtDlrStatus.setInt(3,code);
								        	   pstmtDlrStatus.setInt(4, list.get(0).getCount());
								        	   pstmtDlrStatus.setInt(5, list.get(0).getMessageLength());
								        	   pstmtDlrStatus.setString(6, list.get(0).getMessage());
								        	   pstmtDlrStatus.setString(7, messId);							        	 
								        	   pstmtDlrStatus.setString(8, mobile);
								        	   pstmtDlrStatus.setString(9, list.get(0).getRoute());
								        	   pstmtDlrStatus.setInt(10, 1);
								        	   pstmtDlrStatus.setInt(11, list.get(0).getUserId());
								        	   pstmtDlrStatus.setInt(12, 0);
								               pstmtDlrStatus.addBatch();
								               
								               pstmtQueuedSms1.setInt(1, list.get(0).getId());
								               pstmtQueuedSms1.setString(2,"MT");
								               pstmtQueuedSms1.setString(3,list.get(0).getSender());
								               pstmtQueuedSms1.setString(4, mobile);
								               pstmtQueuedSms1.setString(5, list.get(0).getMessage());
								               pstmtQueuedSms1.setString(6,list.get(0).getRoute());
								               pstmtQueuedSms1.setString(7,"sqlbox");
								               pstmtQueuedSms1.setInt(8, 1);							             
								               pstmtQueuedSms1.setInt(9, code);
								               pstmtQueuedSms1.setInt(10,19);
								               pstmtQueuedSms1.setString(11, messId);
								               pstmtQueuedSms1.setString(12,"UTF-8");
								               pstmtQueuedSms1.addBatch(); 
				
								           }
							        	   
							           }
									   if(queuedSmsList2.size() > 0){
							        	   for(String mobile : queuedSmsList2){
							        			
								        	   String messId = Global.randomString(10);	
								        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
								        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
								        	   pstmtDlrStatus.setInt(3,code);
								        	   pstmtDlrStatus.setInt(4, list.get(0).getCount());
								        	   pstmtDlrStatus.setInt(5, list.get(0).getMessageLength());
								        	   pstmtDlrStatus.setString(6, list.get(0).getMessage());
								        	   pstmtDlrStatus.setString(7, messId);							        	 
								        	   pstmtDlrStatus.setString(8, mobile);
								        	   pstmtDlrStatus.setString(9, list.get(0).getRoute());
								        	   pstmtDlrStatus.setInt(10, 1);
								        	   pstmtDlrStatus.setInt(11, list.get(0).getUserId());
								        	   pstmtDlrStatus.setInt(12, 0);
								               pstmtDlrStatus.addBatch();
								               
								               pstmtQueuedSms2.setInt(1, list.get(0).getId());
								               pstmtQueuedSms2.setString(2,"MT");
								               pstmtQueuedSms2.setString(3,list.get(0).getSender());
								               pstmtQueuedSms2.setString(4, mobile);
								               pstmtQueuedSms2.setString(5, list.get(0).getMessage());
								               pstmtQueuedSms2.setString(6,list.get(0).getRoute());
								               pstmtQueuedSms2.setString(7,"sqlbox");
								               pstmtQueuedSms2.setInt(8, 1);							             
								               pstmtQueuedSms2.setInt(9, code);
								               pstmtQueuedSms2.setInt(10,19);
								               pstmtQueuedSms2.setString(11, messId);
								               pstmtQueuedSms2.setString(12,"UTF-8");
								               pstmtQueuedSms2.addBatch(); 
				
								           }
							        	   
							           }
   
						           }

						           if(mobileListFake.size() > 0){
						        	   for(String mobile : mobileListFake){	        	   
								       		
							        	   String messId = Global.randomString(10);	
							        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
							        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
							        	   pstmtDlrStatus.setInt(3,code);
							        	   pstmtDlrStatus.setInt(4, list.get(0).getCount());
							        	   pstmtDlrStatus.setInt(5, list.get(0).getMessageLength());
							        	   pstmtDlrStatus.setString(6, list.get(0).getMessage());
							        	   pstmtDlrStatus.setString(7, messId);
							        	   pstmtDlrStatus.setString(8, mobile);
							        	   pstmtDlrStatus.setString(9, "mobiF");
							        	   pstmtDlrStatus.setInt(10, 1);
							        	   pstmtDlrStatus.setInt(11, list.get(0).getUserId());
							        	   pstmtDlrStatus.setInt(12, 0);
							               pstmtDlrStatus.addBatch();

							           }
						           }
						           conn.setAutoCommit(false);
						           pstmtDlrStatus.executeBatch();
						           pstmtQueuedSms.executeBatch();
						           pstmtQueuedSms1.executeBatch();
						           pstmtQueuedSms2.executeBatch();
						           String sql1 = "UPDATE user_jobs set job_status = :status WHERE user_id = :userId and id = :id";
						           org.hibernate.Query qry1 = session.createSQLQuery(sql1);
						           qry1.setParameter("status", 2);
						           qry1.setParameter("userId", list.get(0).getUserId());
						           qry1.setParameter("id", list.get(0).getId());			
						           temp =qry1.executeUpdate();
						           conn.commit();
						           conn.setAutoCommit(true);
						           
						         } catch (FileNotFoundException e) {									
									e.printStackTrace();
									conn.rollback();
								} catch (IOException e) {									
									e.printStackTrace();
									conn.rollback();
								} 
						        finally{
						        	if (pstmtDlrStatus != null) {
						        	        try {
						        	        	pstmtDlrStatus.close();
						        	        } catch (SQLException e) { e.printStackTrace();}
						        	    }
					        	    if (pstmtQueuedSms != null) {
					        	        try {
					        	        	pstmtQueuedSms.close();
					        	        } catch (SQLException e) { e.printStackTrace();}
					        	    }
					        	    if (pstmtQueuedSms1 != null) {
					        	        try {
					        	        	pstmtQueuedSms.close();
					        	        } catch (SQLException e) { e.printStackTrace();}
					        	    }
					        	    if (pstmtQueuedSms2 != null) {
					        	        try {
					        	        	pstmtQueuedSms.close();
					        	        } catch (SQLException e) { e.printStackTrace();}
					        	    }
					        	    if (conn != null) {
					        	        try {
					        	            conn.close();
					        	        } catch (SQLException e) {e.printStackTrace();}
					        	    }
						        }                                
					     }
					});
						
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			else
			{
				return temp = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
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
				    	  int messageType = (int)mapList.get("messageType");
				    	  int coding = 0;
				    	  if(messageType == 1)
				    	  {
				    		coding = 0;  
				    	  }else if(messageType == 2)
				    	  {
				    		  coding = 4;
				    	  }else if(messageType == 3)
				    	  {
				    		  coding = 2;
				    	  }
				    	  int messageCount = (int)mapList.get("messageCount");
				    	  int length = (int)mapList.get("messageLength");
				    	  String message = (String)mapList.get("message");
				    	  System.out.println(messageCount);

				    	  int mobi_class = 1;
				    	  String providerId = (String)mapList.get("routeName");
				    	  int type = 1;
				    	  int  userId = (int)mapList.get("userId");
				    	  String momt = "MT";
				    	  String boxcId = "sqlbox";
				    	  int service =1;
				    	  int mClass = 1;
				    	  int dlrMask = 19;
				    	  String chaset = "UTF-8";
				    	  
				          PreparedStatement pstmtDlrStatus = null;
				          PreparedStatement pstmtQueuedSms = null;
				          try{
				           String sqlInsertDlrStatus = "INSERT INTO dlr_status(job_id,Sender, coding, count,length, message, message_id,  mobile, provider_id, type, user_id,mobi_class) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				           pstmtDlrStatus = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus );
				           
				           String sqlInsertQueued ="INSERT INTO queued_sms_quick(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service,coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
				        	   pstmtDlrStatus.setString(8, mobile);
				        	   pstmtDlrStatus.setString(9,providerId);
				        	   pstmtDlrStatus.setInt(10, type);
				        	   pstmtDlrStatus.setInt(11, userId);
				        	   pstmtDlrStatus.setInt(12, 0);
				               pstmtDlrStatus.addBatch();
				               
				               pstmtQueuedSms.setInt(1, jobId);
				               pstmtQueuedSms.setString(2,momt);
				               pstmtQueuedSms.setString(3,sender);
				               pstmtQueuedSms.setString(4, mobile);
				               pstmtQueuedSms.setString(5, message);
				               pstmtQueuedSms.setString(6,providerId);
				               pstmtQueuedSms.setString(7,boxcId);
				               pstmtQueuedSms.setInt(8, service);				            
				               pstmtQueuedSms.setInt(9, coding);
				               pstmtQueuedSms.setInt(10,dlrMask);
				               pstmtQueuedSms.setString(11, messId);
				               pstmtQueuedSms.setString(12,chaset);
				               pstmtQueuedSms.addBatch();

				           }
				           conn.setAutoCommit(false);
				           pstmtDlrStatus.executeBatch();
				           pstmtQueuedSms.executeBatch();		          
				           conn.commit();
				           conn.setAutoCommit(true);
				           temp=1;
				           
				         }catch(Exception e){
				        	 conn.rollback();
				        	 e.printStackTrace();
				          }
				         finally{
				        	    if (pstmtDlrStatus != null) {
				        	        try {
				        	        	pstmtDlrStatus.close();
				        	        	
				        	        } catch (SQLException e) {e.printStackTrace();}
				        	    }
				        	    if (pstmtQueuedSms != null) {
				        	        try {
				        	        	pstmtQueuedSms.close();
				        	        	
				        	        } catch (SQLException e) {e.printStackTrace();	}
				        	    }
				        	    if (conn != null) {
				        	        try {
				        	            conn.close();
				        	           
				        	        } catch (SQLException e) {e.printStackTrace();}
				        	    }
				         }
			     }

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return temp;
	}

	@Override
	public List<UserJobs> userJobsCheck(int jobStatus, int schedualStatus) {
		final Session session = sessionFactory.openSession();
		List<UserJobs> list = null;
		try {
			Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("jobStatus", jobStatus))
			.add(Restrictions.eq("scheduleStatus", schedualStatus))
			.add(Restrictions.ne("jobType", 5))
			.setFirstResult(0);
			list = criteria.list();
			System.out.println("List Size data"+list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<UserJobs> userJobsCheckSchedule(int jobStatus, int schedualStatus, Date dateTime) {
		final Session session = sessionFactory.openSession();
		List<UserJobs> list = null;
		try {
			Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("jobStatus", jobStatus))
			.add(Restrictions.eq("scheduleStatus", schedualStatus))
			.add(Restrictions.le("scheduledAt", dateTime))
			.add(Restrictions.ne("jobType", 5))
			.setFirstResult(0);
			list = criteria.list();		
			/*String sql = "SELECT * FROM user_jobs WHERE job_status = 0 AND scheduled_at = '"+dateTime+"' AND schedule_status=1";
			org.hibernate.Query qry = session.createSQLQuery(sql);			
			list = qry.list();*/
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}
	
	 public static <T> List<List<T>> chunk(List<T> input, int chunkSize) {

	        int inputSize = input.size();
	        int chunkCount = (int) Math.ceil(inputSize / (double) chunkSize);

	        Map<Integer, List<T>> map = new HashMap<>(chunkCount);
	        List<List<T>> chunks = new ArrayList<>(chunkCount);

	        for (int i = 0; i < inputSize; i++) {

	            map.computeIfAbsent(i / chunkSize, (ignore) -> {

	                List<T> chunk = new ArrayList<>();
	                chunks.add(chunk);
	                return chunk;

	            }).add(input.get(i));
	        }

	        return chunks;
	    }

	@Override
	public List<UserJobs> userJobsCheckPersonalized(int jobStatus, int jobType) {
		final Session session = sessionFactory.openSession();
		List<UserJobs> list = null;
		try {
			Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("jobStatus", jobStatus))
			.add(Restrictions.eq("jobType", jobType))
			.setFirstResult(0);
			list = criteria.list();
			System.out.println("List Size data"+list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int savePersonalizedDlrStatus(List<UserJobs> list) throws FileNotFoundException, IOException {
		final List<UserAuthrization> listCheckAutherization = smsHelperService.getUserAuthrizationCheck(list.get(0).getUserId(),list.get(0).getProductId());
		System.out.println("file_list: "+list.get(0));
		final Session session = sessionFactory.openSession();
		
		try {
		
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
										System.out.println("FIle naME"+fileName);										
										InputStream ExcelFileToRead = new FileInputStream(fileName);
										HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);										
										HSSFSheet sheet=wb.getSheetAt(0);
										HSSFRow row; 
										HSSFCell cell;										
										Iterator rows = sheet.rowIterator();
										
										String sqlInsertDlrStatus = "INSERT INTO dlr_status(job_id,Sender, coding, count,length, message, message_id,  mobile, provider_id, type, user_id,mobi_class) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
								        pstmtDlrStatus = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus );
								           
								        String sqlInsertQueued ="INSERT INTO queued_sms(id,momt,sender,receiver,msgdata,smsc_id,boxc_id,service,coding,dlr_mask,dlr_url,charset) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
								        pstmtQueuedSms = (PreparedStatement)conn.prepareStatement(sqlInsertQueued);
								        int code = 0;
										   if(list.get(0).getMessageType() == 1)
										   {
											   code =0;
										   }else if(list.get(0).getMessageType() == 2)
										   {
											   code = 4;
										   }else if(list.get(0).getMessageType() == 3)
										   {
											   code = 2;
										   }
										  int mobi_class = 1;
								    	  String providerId = list.get(0).getRoute();
								    	  int type = 1;
								    	  int  userId = list.get(0).getUserId();
								    	  String momt = "MT";
								    	  String boxcId = "sqlbox";
								    	  int service =1;
								    	  int mClass = 1;
								    	  int dlrMask = 19;
								    	  String chaset = "UTF-8";
									    
										while (rows.hasNext())
										{
											int messageLength = 0;
								    		int messageCount = 0;
								    		String message = "";
								    		String mobile = "";
											row=(HSSFRow) rows.next();
											@SuppressWarnings("rawtypes")
											Iterator cells = row.cellIterator();
											
											int index = 0;
											while (cells.hasNext())
											{
												cell=(HSSFCell) cells.next();
												if (index == 0)
												{
													System.out.print(cell.getStringCellValue()+"- Mobile");
													mobile = cell.getStringCellValue();
												}												
												if(index ==1)
												{
													System.out.print(cell.getStringCellValue()+"- Message");
													message = cell.getStringCellValue();
													messageLength =  cell.getStringCellValue().length();
													messageCount = smsHelperService.messageCount(list.get(0).getMessageType(), messageLength);
												}	
												index++;
											}
											String messId = Global.randomString(10);    	   
								        	   pstmtDlrStatus.setInt(1, list.get(0).getId());
								        	   pstmtDlrStatus.setString(2, list.get(0).getSender());
								        	   pstmtDlrStatus.setInt(3,code);
								        	   pstmtDlrStatus.setInt(4, messageCount);
								        	   pstmtDlrStatus.setInt(5, messageLength);
								        	   pstmtDlrStatus.setString(6, message);
								        	   pstmtDlrStatus.setString(7, messId);
								        	  // 
								        	   pstmtDlrStatus.setString(8, mobile);
								        	   pstmtDlrStatus.setString(9,providerId);
								        	   pstmtDlrStatus.setInt(10, type);
								        	   pstmtDlrStatus.setInt(11, userId);
								        	   pstmtDlrStatus.setInt(12, mobi_class);
								               pstmtDlrStatus.addBatch();
								               
								               pstmtQueuedSms.setInt(1, list.get(0).getId());
								               pstmtQueuedSms.setString(2,momt);
								               pstmtQueuedSms.setString(3,list.get(0).getSender());
								               pstmtQueuedSms.setString(4, mobile);
								               pstmtQueuedSms.setString(5, message);
								               pstmtQueuedSms.setString(6,providerId);
								               pstmtQueuedSms.setString(7,boxcId);
								               pstmtQueuedSms.setInt(8, service);
								              // pstmtQueuedSms.setInt(9, mobi_class);
								               pstmtQueuedSms.setInt(9, code);
								               pstmtQueuedSms.setInt(10,dlrMask);
								               pstmtQueuedSms.setString(11, messId);
								               pstmtQueuedSms.setString(12,chaset);
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
									}								
								   						           
						         } catch (FileNotFoundException e) {									
									e.printStackTrace();
									conn.rollback();
								} catch (IOException e) {									
									e.printStackTrace();
									conn.rollback();
								} 
						        finally{
						        	if (pstmtDlrStatus != null) {
						        	        try {
						        	        	pstmtDlrStatus.close();
						        	        } catch (SQLException e) { e.printStackTrace();}
						        	    }
					        	    if (pstmtQueuedSms != null) {
					        	        try {
					        	        	pstmtQueuedSms.close();
					        	        } catch (SQLException e) { e.printStackTrace();}
					        	    }
					        	    if (conn != null) {
					        	        try {
					        	            conn.close();
					        	        } catch (SQLException e) {e.printStackTrace();}
					        	    }
						        }                                
					     }
					});
						
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();					
					
				}
			}
			else
			{
				return temp = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();	
		}
		finally {
			try {
				if(session != null)
				{
					session.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();	
			}
		}
		return temp;
	}
	
}
