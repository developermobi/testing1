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
			} catch (Exception e) {
				e.printStackTrace();
				temp = 0;
				tx.rollback();
			}finally {
				session.close();
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
		Criteria criteria = session.createCriteria(Contact.class);
		criteria.add(Restrictions.eq("userId", userId))
		.add(Restrictions.eq("status", 1))
		.setFirstResult(start)
		.setMaxResults(limit);
		List<Contact> list = criteria.list();
		session.close();
		return list;
		
	}

	@Override
	public List<Contact> getContactCountByUserId(int userId) {
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Contact.class);
		criteria.add(Restrictions.eq("userId", userId))
		.add(Restrictions.eq("status", 1));
		List<Contact> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public List<Contact> getContactByContactId(int contactId) {
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Contact.class);
		criteria.add(Restrictions.eq("contactId", contactId))
		.add(Restrictions.eq("status", 1));
		List<Contact> list = criteria.list();
		session.close();
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
			session.close();
		}
		
		return temp;
	}

	@Override
	public int deleteContactByContactId(int contactId) {
		int temp = 0;
		session = sessionFactory.openSession();
		tx = session.beginTransaction();		
		try {	
			Contact contact =(Contact)session.get(Contact.class,contactId);
			if(contact.getContactId() == contactId)
			{
				int status =2;
				String sql = "UPDATE Contact set status = :status WHERE id = :contactId";
				Query qry = session.createQuery(sql);
				qry.setParameter("status", status);
				qry.setParameter("contactId", contactId);
				temp = qry.executeUpdate();
				System.out.println(temp);
				temp=1;
				tx.commit();
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
			session.close();
		}			


		return temp;
	}

	@Override
	public int uploadMultipleContact(int groupId, final int userId, final CSVReader reader) {
		
		 try {
			
		        
			 	session=sessionFactory.openSession();
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
					            
					            System.out.println(nextLine[0]+"  "+nextLine[1]+"  "+nextLine[2]+"  "+nextLine[3]);	
					            
					            pstmtContact.setString(1, nextLine[3]);
					            pstmtContact.setString(2, nextLine[2]);
					            pstmtContact.setString(3, nextLine[1]);
					            pstmtContact.setString(4, nextLine[0]);
					            pstmtContact.setInt(5, 1);
					            pstmtContact.setInt(6, userId);
					            pstmtContact.setInt(7,groupDetails.getGroupId());
					        }
				           
				           conn.setAutoCommit(false);
				           pstmtContact.executeBatch();
				           conn.commit();
				           conn.setAutoCommit(true);
				           
				         } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
				         finally{
				        	 pstmtContact .close();
				         }                                
				     }

				});
		        
		        
		        
		        
		        
		        
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	
}
