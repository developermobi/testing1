package com.mobisoft.sms.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobisoft.sms.model.SenderId;
import com.mobisoft.sms.model.User;

@Repository("senderIdDao")
public class SenderIdDaoImpl implements SenderIDDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session session = null;
	
	private Transaction tx = null;
	
	
	@Override
	public int saveSenderId(SenderId senderId) {
		session =  sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {
			session.saveOrUpdate(senderId);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<SenderId> getSenderId() {
		session = sessionFactory.openSession();
		List<SenderId> list = null;
		try {
			list = session.createCriteria(SenderId.class).addOrder(Order.desc("id")).list();
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
	public List<SenderId> getSenderIdByUserId(int userId) {
		session = sessionFactory.openSession();
		List<SenderId> list = null;
		try {
			User user = (User)session.load(User.class,userId);
			Criteria criteria = session.createCriteria(SenderId.class);
			criteria.add(Restrictions.eq("userId", user))
			.add(Restrictions.eq("status", 1))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
			list = criteria.list();
			session.flush();
			session.clear();
			//session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
			/*try {
				if(session != null)
				{
					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}*/
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SenderId> getSenderId(int senderId) {
		session = sessionFactory.openSession();
		List<SenderId> list = null;
		try {
			Criteria criteria = session.createCriteria(SenderId.class);
			criteria.add(Restrictions.eq("id", senderId)).add(Restrictions.eq("status", 1));		
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
	public int updateSenderId(SenderId senderId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {	
			String sql = "update sender_id SET sender_id = :senderId,status = :status WHERE id = :id";			
			Query qry = session.createSQLQuery(sql);
			qry.setParameter("senderId", senderId.getSender_id());
			qry.setParameter("status", senderId.getStatus());
			qry.setParameter("id",senderId.getId());
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
	public int deleteSenderId(SenderId senderId) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {
			Object object = session.load(SenderId.class,new Integer(senderId.getId()));
			SenderId senderId2=(SenderId)object;			
			session.delete(senderId2);
			temp =1;
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
	public List<SenderId> getSenderIdByUserIdPaginate(int userId, int start, int limit) {
		session = sessionFactory.openSession();
		List<SenderId> list = null;
		try {
			User user = (User)session.load(User.class,userId);
			Criteria criteria = session.createCriteria(SenderId.class);
			criteria.add(Restrictions.eq("userId", user))
			//.add(Restrictions.eq("status", 1))
			.setFirstResult(start)
			.setMaxResults(limit).addOrder(Order.desc("id"))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
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

}
