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

import com.mobisoft.sms.model.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	SessionFactory sessionFactory;
	public int saveUser(User user) {
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			session.saveOrUpdate(user);
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
	public List<User> getUser() {
		Session session = sessionFactory.openSession();		
		List<User> list = session.createCriteria(User.class).list();
		session.close();
		return list;
	}
	@Override
	public List<User> getAdminById(int userId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("id", userId));		
		List<User> list = criteria.list();
		session.close();
		return list;
	}
	@Override
	public int updateAdmin(User admin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			
			String sql = "UPDATE User SET user_name = :userName,address = :address,city = :city,company_name = :company_name,country = :country,email = :email,mobile = :mobile, name = :name,role = :role,state = :state, status = :status WHERE id = :id";
			
			Query qry = session.createQuery(sql);
			qry.setParameter("user_name", admin.getUserName());
			qry.setParameter("address", admin.getAddress());
			qry.setParameter("city", admin.getCity());
			qry.setParameter("company_name", admin.getCompany_name());
			qry.setParameter("country", admin.getCountry());
			qry.setParameter("email", admin.getEmail());
			qry.setParameter("mobile", admin.getMobile());
			qry.setParameter("name", admin.getName());
			qry.setParameter("role", admin.getRole());
			qry.setParameter("state", admin.getState());
			qry.setParameter("status", admin.getStatus());
			
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
	public int deleteAdmin(User admin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {
			
			String sql = "UPDATE Admin status = :status WHERE id = :userId";
			
			Query qry = session.createQuery(sql);
			qry.setParameter("status", admin.getStatus());
			qry.setParameter("userId", admin.getId());		
			
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
}
