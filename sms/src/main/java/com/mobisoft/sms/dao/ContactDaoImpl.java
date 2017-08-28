package com.mobisoft.sms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Contact;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;
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
}
