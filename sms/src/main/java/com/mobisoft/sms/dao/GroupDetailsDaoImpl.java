package com.mobisoft.sms.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Contact;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;

@Repository("groupDetailsDao")
public class GroupDetailsDaoImpl implements GroupDetailsDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session session = null;
	
	private Transaction tx = null;
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
			//session.close();
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
	public List<GroupDetails> getGroupDetailsByUserId(int userId,int start, int limit) {
		session = sessionFactory.openSession();
		List<GroupDetails> list = null;
		try {
			User user= (User)session.get(User.class, userId);
			Criteria criteria = session.createCriteria(GroupDetails.class);
			criteria.add(Restrictions.eq("userId", user))
			.add(Restrictions.ne("status", 2))
			.setFirstResult(start)
			.setMaxResults(limit);			
			list = criteria.list();
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
		return list;
	}

	@Override
	public List<GroupDetails> getGroupDetailsByGroupId(int groupId) {
		session = sessionFactory.openSession();
		List<GroupDetails> list = null;
		try {
			Criteria criteria = session.createCriteria(GroupDetails.class);
			criteria.add(Restrictions.eq("groupId", groupId))
			.add(Restrictions.ne("status", 2));
			list = criteria.list();
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
			//session.close();
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
			//session.close();
			
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

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupDetails> getGroupDetailsCountByUserId(int userId) {
		
		session= sessionFactory.openSession();
		List<GroupDetails> list = null;
		try {
			User user= (User)session.get(User.class, userId);
			Criteria criteria = session.createCriteria(GroupDetails.class);
			criteria.add(Restrictions.eq("userId", user))
			.add(Restrictions.ne("status", 2));			
			list = criteria.list();
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
				
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<GroupDetails> getActiveGroupDetailsByUserId(int userId) {
		session = sessionFactory.openSession();
		List<GroupDetails> list = null;
		try {
			User user= (User)session.get(User.class, userId);
			Criteria criteria = session.createCriteria(GroupDetails.class);
			criteria.add(Restrictions.eq("userId", user))
			.add(Restrictions.eq("status", 1));
			list = criteria.list();
			session.flush();
			session.clear();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		finally {
			//
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
