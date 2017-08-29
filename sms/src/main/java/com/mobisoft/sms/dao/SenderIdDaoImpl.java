package com.mobisoft.sms.dao;

import java.nio.channels.SeekableByteChannel;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.SenderId;
import com.mobisoft.sms.model.Template;
import com.mobisoft.sms.model.User;

@Repository("senderIdDao")
public class SenderIdDaoImpl implements SenderIDDao{

	@Autowired
	SessionFactory sessionFactory;
	@Override
	public int saveSenderId(SenderId senderId) {
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;		
		try {
			session.saveOrUpdate(senderId);
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
	public List<SenderId> getSenderId() {
		Session session = sessionFactory.openSession();		
		List<SenderId> list = session.createCriteria(SenderId.class).list();
		session.close();
		return list;
	}

	@Override
	public List<SenderId> getSenderIdByUserId(int userId) {
		Session session = sessionFactory.openSession();
		User user = (User)session.load(User.class,userId);
		Criteria criteria = session.createCriteria(SenderId.class);
		criteria.add(Restrictions.eq("userId", user)).add(Restrictions.eq("status", 1)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
		List<SenderId> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public List<SenderId> getSenderId(int senderId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(SenderId.class);
		criteria.add(Restrictions.eq("id", senderId)).add(Restrictions.eq("status", 1));		
		List<SenderId> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public int updateSenderId(SenderId senderId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {	
			String sql = "update sender_id SET sender_id = :senderId,status = :status WHERE id = :id";			
			Query qry = session.createSQLQuery(sql);
			qry.setParameter("senderId", senderId.getSender_id());
			qry.setParameter("status", senderId.getStatus());
			qry.setParameter("id",senderId.getId());
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
	public int deleteSenderId(SenderId senderId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;		
		try {
			Object object = session.load(SenderId.class,new Integer(senderId.getId()));
			SenderId senderId2=(SenderId)object;			
			session.delete(senderId2);
			temp =1;
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
	public List<SenderId> getSenderIdByUserIdPaginate(int userId, int start, int limit) {
		Session session = sessionFactory.openSession();
		User user = (User)session.load(User.class,userId);
		Criteria criteria = session.createCriteria(SenderId.class);
		criteria.add(Restrictions.eq("userId", user))
		.add(Restrictions.eq("status", 1))
		.setFirstResult(start)
		.setMaxResults(limit)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
		List<SenderId> list = criteria.list();
		session.close();
		return list;
	}

}
