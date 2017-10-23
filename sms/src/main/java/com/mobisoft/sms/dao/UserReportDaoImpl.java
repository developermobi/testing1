package com.mobisoft.sms.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.utility.Global;

import au.com.bytecode.opencsv.CSVWriter;

@Repository("userReportDao")
public class UserReportDaoImpl implements UserReportDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session session = null;
	private Transaction tx = null;	
	
	@Value("${downloadUserCsvFile}")
	private String downloadUserCsvFile;
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List todayCountMessage(int userId) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		System.out.println(dtf.format(localDate));
		session = sessionFactory.openSession();
		List results = null;
		try {
			String sql = "SELECT sum(count) ,status  FROM dlr_status WHERE user_id = "+userId+" and 	logged_at LIKE '%"+localDate+"%' group by status";
			Query query = session.createSQLQuery(sql);
			results = query.list();
			//session.close();
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
		return results;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List weeklyCountMessage(int userId) {
		session = sessionFactory.openSession();	
		List results = null;
		try {
			String sql = "SELECT sum(count),status FROM dlr_status WHERE `logged_at` > DATE_SUB(NOW(), INTERVAL 1 WEEK) AND user_id ="+userId+" GROUP BY status ";
			Query query = session.createSQLQuery(sql);
			results = query.list();
			//session.close();
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
		return results;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List monthlyCountMessage(int userId) {
		session = sessionFactory.openSession();
		List results = null;
		try {
			String sql = "SELECT sum(count),status FROM dlr_status WHERE `logged_at` > DATE_SUB(NOW(), INTERVAL 2 MONTH) AND user_id ="+userId+" GROUP BY status";
			Query query = session.createSQLQuery(sql);
			results = query.list();
			//session.close();
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
		return results;
	}

	@Override
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max) {
		
		session = sessionFactory.openSession();
		List<DlrStatus> listResult = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = dtf.format(localDate);
		System.out.println(dtf.format(localDate));
		try {
			//Criteria criteria = session.createCriteria(DlrStatus.class);
			//criteria.add(Restrictions.eq("userId",userId)).setFirstResult(start).setMaxResults(max).addOrder(Order.desc("loggedAt"));
			
			String sql = "SELECT * FROM dlr_status WHERE user_id = "+userId+" and 	logged_at LIKE '%"+date+"%'  order by id desc limit "+start+","+max+"";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(DlrStatus.class);
			listResult = query.list();
			//session.close();
			
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
		return listResult;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public List archiveReportByUserId(final int userId,final String startDate,final String endDate) {
		System.out.println("in");
		session = sessionFactory.openSession();
		final List list = new ArrayList<String>();
		session.doWork(new Work() {			   
		    @SuppressWarnings("unchecked")
			@Override
		       public void execute(Connection conn) throws SQLException {
		         Statement pstmtDlrStatus = conn.createStatement();
		         System.out.println("in conn");
		          try{
		        	  
		        	  
		           String sqlSelectDataDlrStatus = "SELECT mobile, Sender, message, status, logged_at, dlr_time FROM dlr_status WHERE  DATE_FORMAT(logged_at,'%Y-%m-%d') >= '"+startDate+"' AND DATE_FORMAT(logged_at,'%Y-%m-%d')  <= '"+endDate+"' AND user_id="+userId+"";
		           System.out.println(sqlSelectDataDlrStatus);
		           ResultSet rs = pstmtDlrStatus.executeQuery(sqlSelectDataDlrStatus);
		           //Global.convertToCsv(rs, uploadCsvTextFile+"number.csv");
		           Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		           long time = cal.getTimeInMillis();
		           
		           /* String rootPath = System.getProperty("catalina.home");
		              File dir = new File(rootPath + File.separator + "downloadUserCsvFile");
		           File file =null;
					if (!dir.exists())
					{
						dir.mkdirs();  	  
					}
					file = new File(dir.getAbsolutePath()+"/"+fileName);*/
		           String fileName = "Report"+time+".csv";
		           File dir = new File(downloadUserCsvFile);
		           File file =null;
					if (!dir.exists())
					{
						dir.mkdirs();  	  
					}
					file = new File(dir.getAbsolutePath()+"/"+fileName);
					CSVWriter writer = new CSVWriter(new FileWriter(file));
					String[] header="Mobile,Sender,Message,Status,logged_at,dlr_time".split(",");
					//writer.writeAll(allLines);(header);
					writer.writeAll(rs, true); //And the second argument is boolean which represents whether you want to write header columns (table column names) to file or not.
					writer.close();
					
					list.add(fileName);
		           /*while(rs.next())
		           {
		        	   
		        	   String message = rs.getString("message");
		        	   message = message.replaceAll(",", "");
		        	   list.add(rs.getString("mobile"));
		        	   list.add(rs.getString("Sender"));
		        	   list.add(message);
		        	   list.add(rs.getString("STATUS"));
		        	   list.add(rs.getString("logged_at"));
		        	   list.add(rs.getString("dlr_time"));
       	   
		           }*/
		           conn.setAutoCommit(false);
		           conn.commit();
		           conn.setAutoCommit(true);
		           
		         }catch(Exception e)
		          {
		        	 e.printStackTrace();
		          }
		         finally{
		        	 try {
						if(pstmtDlrStatus != null)
						{
							pstmtDlrStatus.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}try {
						if(conn != null)
						{
							conn.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
		        			        	 
		         }                                
		     }
		});
		return list;
		
	}
	@Override
	public List<UserJobs> scheduleReportByUserId(int userId,int start,int max) {
		List<UserJobs> listResult = null;
		session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("scheduleStatus", 1)).setFirstResult(start).setMaxResults(max).addOrder(Order.desc("scheduledAt"));
			listResult = criteria.list();
			//session.close();
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
		return listResult;
		
	}
	@Override
	public List<Integer> messageCountDaily(int userId, String date) {
		List<Integer> results = null;
		session = sessionFactory.openSession();
		try {
			String sql = "SELECT count(mobile)  FROM dlr_status WHERE user_id = "+userId+" and 	logged_at LIKE '%"+date+"%'";
			Query query = session.createSQLQuery(sql);
			results = query.list();			
			//session.close();
			
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
		return results;
	}
	@Override
	public int messageCountScheduale(int userId) {
		int countSchedual =0;
		session = sessionFactory.openSession();
		List<UserJobs> listResult = null;		
		try {
			Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("scheduleStatus", 1)).addOrder(Order.desc("scheduledAt"));
			listResult = criteria.list();
			countSchedual = listResult.size();
			//session.close();
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
		return countSchedual;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<UserJobs> compaignStatus(int userId, int start, int max,String date) {
		
		session = sessionFactory.openSession();
		List<UserJobs> listResult = null;		
		try {
			/*Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("scheduleStatus", 0))
			.addOrder(Order.desc("queuedAt")).setFirstResult(start).setMaxResults(max);*/
			
			SQLQuery query = session.createSQLQuery("select * from user_jobs where user_id ="+userId+" and  completed_at like '%"+date+"%'");	
		    query.addEntity(UserJobs.class);
			listResult = query.list();
			//session.close();
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
		return listResult;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<UserJobs> compaignStatusCount(int userId,String date) {
		session = sessionFactory.openSession();
		List<UserJobs> listResult = null;		
		try {
			/*Criteria criteria = session.createCriteria(UserJobs.class);				
			criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("scheduleStatus", 0))
			.setProjection(Projections.rowCount());
			listResult = criteria.list();*/
			SQLQuery query = session.createSQLQuery("select count(*) as count from user_jobs where user_id ="+userId+" and  completed_at like '%"+date+"%'");	
		   
			listResult = query.list();
			
			//session.close();
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
		return listResult;
	}
	@Override
	public List<DlrStatus> dlrStatusGroupBy(int jobId, int userId) {
		session = sessionFactory.openSession();
		List<DlrStatus> listResult = null;		
		try {
			 Query q = session.createSQLQuery("SELECT STATUS,COUNT(MOBILE) AS mobile FROM dlr_status WHERE job_id = "+jobId+" and user_id="+userId+" GROUP BY STATUS");
			    //q.addEntity(DlrStatus.class);			   
			listResult = q.list();
			//session.close();
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
		return listResult;
	}
	@Override
	public List dlrStausRepotExportDetails(int userId, int jobId, String status) {
		
		System.out.println("in");
		session = sessionFactory.openSession();
		final List list = new ArrayList<String>();
		int temp = 0;
		
		session.doWork(new Work() {			   
		    @SuppressWarnings("unchecked")
			@Override
		       public void execute(Connection conn) throws SQLException {
		         Statement pstmtDlrStatus = conn.createStatement();
		         System.out.println("in conn");
		         String sqlSelectDataDlrStatus = "";
		          try{
		        	  
		           if(!status.trim().equals(""))
		           {
		        	   sqlSelectDataDlrStatus ="SELECT mobile, Sender, message, STATUS, logged_at, dlr_time FROM dlr_status WHERE job_id= "+jobId+" AND user_id="+userId+" and status = '"+status+"'";
		           }
		           else
		           {
		        	  sqlSelectDataDlrStatus = "SELECT mobile, Sender, message, STATUS, logged_at, dlr_time FROM dlr_status WHERE job_id= "+jobId+" AND user_id="+userId+"";
		           }		           
		           System.out.println(sqlSelectDataDlrStatus);
		           ResultSet rs = pstmtDlrStatus.executeQuery(sqlSelectDataDlrStatus);
		          /* if(rs != null)
		           {*/
		        	   //Global.convertToCsv(rs, uploadCsvTextFile+"number.csv");
			           Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			           long time = cal.getTimeInMillis();
			           
			           /* String rootPath = System.getProperty("catalina.home");
			              File dir = new File(rootPath + File.separator + "downloadUserCsvFile");
			           File file =null;
						if (!dir.exists())
						{
							dir.mkdirs();  	  
						}
						file = new File(dir.getAbsolutePath()+"/"+fileName);*/
			           String fileName = "Report"+time+".csv";
			           File dir = new File(downloadUserCsvFile);
			           File file =null;
						if (!dir.exists())
						{
							dir.mkdirs();  	  
						}
						file = new File(dir.getAbsolutePath()+"/"+fileName);
						CSVWriter writer = new CSVWriter(new FileWriter(file));
						String[] header="Mobile,Sender,Message,Status,logged_at,dlr_time".split(",");
						//writer.writeAll(allLines);(header);
						writer.writeAll(rs, true); //And the second argument is boolean which represents whether you want to write header columns (table column names) to file or not.
						writer.close();
						
						list.add(fileName);
			           /*while(rs.next())
			           {
			        	   
			        	   String message = rs.getString("message");
			        	   message = message.replaceAll(",", "");
			        	   list.add(rs.getString("mobile"));
			        	   list.add(rs.getString("Sender"));
			        	   list.add(message);
			        	   list.add(rs.getString("STATUS"));
			        	   list.add(rs.getString("logged_at"));
			        	   list.add(rs.getString("dlr_time"));
	       	   
			           }*/
			           conn.setAutoCommit(false);
			           conn.commit();
			           conn.setAutoCommit(true);
			           list.add("1");
		         /*  }
		           else
		           {
		        	   list.add(2);
		           }*/
		          
		         }catch(Exception e)
		          {
		        	 e.printStackTrace();
		          }
		         finally{
		        	 try {
						if(pstmtDlrStatus != null)
						{
							pstmtDlrStatus.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}try {
						if(conn != null)
						{
							conn.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
		        			        	 
		         }                                
		     }
		});
		
		return list;
	}
	@Override
	public Map<Integer, List<DlrStatus>> dlrReportDetails(int userId, int jobId, String status,int start,int max) {
		Map<Integer,List<DlrStatus>> mapList= new HashMap<Integer, List<DlrStatus>>();
		session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(DlrStatus.class);
			criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("jobId",jobId))
			.add(Restrictions.eq("status", status)).setFirstResult(start).setMaxResults(max).addOrder(Order.desc("id"));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("mobile").as("mobile"));
			projectionList.add(Projections.property("Sender").as("sender"));
			projectionList.add(Projections.property("message").as("message"));
			projectionList.add(Projections.property("loggedAt").as("loggedAt"));
			projectionList.add(Projections.property("dlrTime").as("dlrTime"));
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<DlrStatus> list = criteria.list();
			
			//total count
			Criteria criteriaCount = session.createCriteria(DlrStatus.class);
			criteriaCount.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("jobId",jobId))
			.add(Restrictions.eq("status", status)).setProjection(Projections.rowCount());
			List<DlrStatus> countList = criteriaCount.list();
			if(countList.size() > 0)
			{
				mapList.put(1,list);
				mapList.put(2, countList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(session != null)
			{
				session.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return mapList;
	}
	@Override
	public List<DlrStatus> searchMobileStatus(String moblieNumber,String date) {
		session = sessionFactory.openSession();
		List<DlrStatus> listResult = null;		
		try {
			/*Criteria criteria = session.createCriteria(UserJobs.class);
			criteria.add(Restrictions.eq("userId",userId)).add(Restrictions.eq("scheduleStatus", 0))
			.addOrder(Order.desc("queuedAt")).setFirstResult(start).setMaxResults(max);*/
			
			SQLQuery query = session.createSQLQuery("select * from dlr_status where mobile ="+moblieNumber+" and  logged_at like '%"+date+"%'");	
		    query.addEntity(DlrStatus.class);
			listResult = query.list();
			//session.close();
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
		return listResult;
	}
	
}
