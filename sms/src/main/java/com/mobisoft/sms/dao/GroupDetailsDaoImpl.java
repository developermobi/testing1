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
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;


@Repository("groupDetailsDao")
public class GroupDetailsDaoImpl implements GroupDetailsDao{

	@Autowired
	SessionFactory sessionFactory;
	
	Session session = null;
	
	Transaction tx = null;
	@Override
	public int saveGroupDetails(JsonNode node) {
		session =  sessionFactory.openSession();
		tx = session.beginTransaction();
		User user = (User)session.get(User.class,node.get("userId").asInt());
		GroupDetails groupDetails = new GroupDetails();
		groupDetails.setName(node.get("name").asText());
		groupDetails.setStatus(node.get("status").asInt());
		groupDetails.setGroupDescription(node.get("groupDescription").asText());
		groupDetails.setUserId(user);
		int temp = 0;		
		try {
			session.saveOrUpdate(groupDetails);					
			temp = 1;
			tx.commit();
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
	public List<GroupDetails> getGroupDetailsByUserId(int userId,int start, int limit) {
		session = sessionFactory.openSession();
		User user= (User)session.get(User.class, userId);
		Criteria criteria = session.createCriteria(GroupDetails.class);
		criteria.add(Restrictions.eq("userId", user))
		.add(Restrictions.eq("status", 1))
		.setFirstResult(start)
		.setMaxResults(limit);
		List<GroupDetails> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public List<GroupDetails> getGroupDetailsByGroupId(int groupId) {
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(GroupDetails.class);
		criteria.add(Restrictions.eq("groupId", groupId))
		.add(Restrictions.eq("status", 1));
		List<GroupDetails> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public int updateGroupDetails(GroupDetails groupDetails,int groupId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;
		try {
			String sql = "UPDATE GroupDetails SET name = :name,status = :status,group_description = :groupDescription WHERE id = :groupId";
			Query qry = session.createQuery(sql);			
			qry.setParameter("name", groupDetails.getName());			
			qry.setParameter("status", groupDetails.getStatus());			
			qry.setParameter("groupDescription", groupDetails.getGroupDescription());
			qry.setParameter("groupId", groupId);
			temp = qry.executeUpdate();
			tx.commit();
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
	public int deleteGroupDetailsByGroupId(int groupId) {
		int temp = 0;
		session = sessionFactory.openSession();
		tx = session.beginTransaction();		
		try {				
			int status =2;
			String sql = "UPDATE GroupDetails set status = :status WHERE id = :groupId";
			Query qry = session.createQuery(sql);
			qry.setParameter("status", status);
			qry.setParameter("groupId", groupId);
			temp = qry.executeUpdate();
			System.out.println(temp);
			temp=1;
			tx.commit();
			
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
	public List<GroupDetails> getGroupDetailsCountByUserId(int userId) {
		System.out.println("Show data");	
		session= sessionFactory.openSession();
		User user= (User)session.get(User.class, userId);
		System.out.println(user.getUserId());
		Criteria criteria = session.createCriteria(GroupDetails.class);
		criteria.add(Restrictions.eq("userId", user))
		.add(Restrictions.eq("status", 1));
		List<GroupDetails> list = criteria.list();
		session.close();
		System.out.println("Show data"+list.size());
		return list;
	}

}
