package com.mobisoft.sms.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Contact;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.utility.Global;
import com.mysql.jdbc.PreparedStatement;

import au.com.bytecode.opencsv.CSVReader;
@Repository("conatctDao")
public class ContactDaoImpl implements ContactDao{

	@Autowired
	SessionFactory sessionFactory;
	
	Session session = null;
	
	Transaction tx = null;
	private int temp=0;

	@Override
	public int saveConact(JsonNode node) {
		int temp = 0;
		session =  sessionFactory.openSession();
		tx = session.beginTransaction();
		GroupDetails groupDetails=(GroupDetails)session.get(GroupDetails.class, node.get("groupId").asInt());
		if(groupDetails.getGroupId() > 0)
		{
			Contact contact = new Contact();
			contact.setDesignation(node.get("designation").asText());
			contact.setEmailId(node.get("emailId").asText());
			contact.setMobile(node.get("mobile").asText());
			contact.setName(node.get("name").asText());
			contact.setStatus(1);
			contact.setUserId(node.get("userId").asInt());
			contact.setGroupId(groupDetails);
					
			try {
				session.saveOrUpdate(contact);					
				temp = 1;
				tx.commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
				temp = 0;
				tx.rollback();
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
		}
		else
		{
			temp = 2;
		}
		
		return temp;
	}

	@Override
	public List<Contact> getContactByUserId(int userId, int start, int limit) {
		session = sessionFactory.openSession();
		List<Contact> list = null;
		try {
			Criteria criteria = session.createCriteria(Contact.class);
			criteria.add(Restrictions.eq("userId", userId))
			.add(Restrictions.ne("status", 2))
			.setFirstResult(start)
			.setMaxResults(limit).addOrder(Order.desc("created"));
			 list = criteria.list();
			 session.close();
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
		
		return list;
		
	}

	@Override
	public List<Contact> getContactCountByUserId(int userId) {
		session = sessionFactory.openSession();
		List<Contact> list = null;
		try {
			Criteria criteria = session.createCriteria(Contact.class).setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("userId", userId))
			.add(Restrictions.ne("status", 2));
			list = criteria.list();
			session.close();
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
		return list;
	}

	@Override
	public List<Contact> getContactByContactId(int contactId) {
		session = sessionFactory.openSession();
		List<Contact> list = null;
		try {
			Criteria criteria = session.createCriteria(Contact.class);
			criteria.add(Restrictions.eq("contactId", contactId))
			.add(Restrictions.ne("status", 2));
			list = criteria.list();
			session.close();
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
		return list;
	}

	@Override
	public int updateContact(JsonNode node, int contactId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;
		try {
			GroupDetails groupDetails = (GroupDetails)session.get(GroupDetails.class, node.get("groupId").asInt());
			if(groupDetails.getGroupId() == node.get("groupId").asInt())
			{
				Contact contact =(Contact)session.get(Contact.class, contactId);
				if(contact.getContactId() == contactId)
				{
					String sql = "UPDATE Contact SET name = :name,status = :status,designation = :designation,email_id=:emailId,mobile=:mobile WHERE id = :contactId";
					Query qry = session.createQuery(sql);			
					qry.setParameter("name", node.get("name").asText());			
					qry.setParameter("status", node.get("status").asInt());			
					qry.setParameter("designation", node.get("designation").asText());
					qry.setParameter("mobile", node.get("mobile").asText());
					qry.setParameter("emailId",node.get("emailId").asText());
					qry.setParameter("contactId", contactId);
					temp = qry.executeUpdate();
					tx.commit();
					session.close();
				}
				else
				{
					temp=3;
				}
				
			}
			else
			{
				temp=2;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			temp = 0;
			tx.rollback();
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
		
		return temp;
	}

	@Override
	public int updateContactStatusByContactId(int contactId, int status) {
		int temp = 0;
		session = sessionFactory.openSession();
		tx = session.beginTransaction();		
		try {	
			Contact contact =(Contact)session.get(Contact.class,contactId);
			if(contact.getContactId() == contactId)
			{				
				String sql = "UPDATE Contact set status = :status WHERE id = :contactId";
				Query qry = session.createQuery(sql);
				qry.setParameter("status", status);
				qry.setParameter("contactId", contactId);
				temp = qry.executeUpdate();
				System.out.println(temp);
				temp=1;
				tx.commit();
				session.close();
			}
			else
			{
				temp =2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			temp = 2;
			tx.rollback();
			
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


		return temp;
	}

	@Override
	public int uploadMultipleContact(int groupId, final int userId, final CSVReader reader) {
		session=sessionFactory.openSession();
		try { 
			
		 
			final GroupDetails groupDetails=(GroupDetails)session.get(GroupDetails.class,groupId);
	        session.doWork(new Work() {				   
			       @Override
			       public void execute(Connection conn) throws SQLException {
			          PreparedStatement pstmtContact = null;
			          try{
			           String sqlInsertDlrStatus = "INSERT INTO contact(designation, email_id, mobile, name, status,user_id, group_id) VALUES (?,?,?,?,?,?,?)";
			           pstmtContact = (PreparedStatement) conn.prepareStatement(sqlInsertDlrStatus );
			           String [] nextLine;
			           int i=0;
			           while ((nextLine = reader.readNext()) != null) {
			        	   			
			            	    pstmtContact.setString(1, nextLine[3]);
					            pstmtContact.setString(2, nextLine[2]);
					            pstmtContact.setString(3, nextLine[1]);
					            pstmtContact.setString(4, nextLine[0]);
					            pstmtContact.setInt(5, 1);
					            pstmtContact.setInt(6, userId);
					            pstmtContact.setInt(7,groupDetails.getGroupId());
					            pstmtContact.addBatch();
					            
				       } 		           
			           reader.close();
					   conn.setAutoCommit(false);
			           pstmtContact.executeBatch();
					   System.out.println("Add Conatct");
					   conn.commit();
					   conn.setAutoCommit(true);
					   
					   temp = 1;
			         } catch (IOException e) {
						e.printStackTrace();
						conn.rollback();
					} 
			          finally {
			  			try {
			  				if(conn != null)
			  				{
			  					conn.close();
			  				}
			  			} catch (Exception e2) {e2.printStackTrace();}
			  			try {
			  				if(pstmtContact != null)
			  				{
			  					pstmtContact.close();
			  				}
			  			} catch (Exception e2) {e2.printStackTrace();}
			  		}		                                
			     }

			});

		        
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
  			try {
  				if(session != null)
  				{
  					session.close();
  				}
  			} catch (Exception e2) {e2.printStackTrace();}
  		}		
		return temp;
	}

	@Override
	public List<Contact> getContactCountByGroupId(int groupId,int start,int max) {
		session = sessionFactory.openSession();
		List<Contact> list = null;
		try {
			GroupDetails groupDetails =(GroupDetails)session.get(GroupDetails.class,groupId);
			Criteria criteria = session.createCriteria(Contact.class);
			criteria.add(Restrictions.eq("groupId", groupDetails))
			.add(Restrictions.ne("status", 2)).setFirstResult(start).setMaxResults(max);
			list = criteria.list();
			session.close();
		} catch (Exception e) {e.printStackTrace();}
		finally {
  			try {
  				if(session != null)
  				{
  					session.close();
  				}
  			} catch (Exception e2) {e2.printStackTrace();}
  		}	
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Contact> countGroupConatct(int groupId) {
		session = sessionFactory.openSession();
		int count = 0;
		List<Contact> list = null;
		try {
			GroupDetails groupDetails =(GroupDetails)session.get(GroupDetails.class,groupId);
			Criteria criteria = session.createCriteria(Contact.class);
			criteria.add(Restrictions.eq("groupId", groupDetails)).setProjection(Projections.rowCount())
			.add(Restrictions.ne("status", 2));
			list = criteria.list();					
			session.close();
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
	
}
