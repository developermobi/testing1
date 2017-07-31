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

import com.mobisoft.sms.model.Template;
import com.mobisoft.sms.model.User;

@Repository("templateDao")
public class TemplateDaoImpl implements TemplateDao{

	@Autowired
	SessionFactory sessionFactory;
	@Override
	public int saveTemplate(Template template) {
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;		
		try {
			session.saveOrUpdate(template);
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
	public List<Template> getTemplate() {
		Session session = sessionFactory.openSession();		
		List<Template> list = session.createCriteria(Template.class).list();
		session.close();
		return list;
	}

	@Override
	public List<Template> getTemplateByUserId(int userId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("u_id", userId)).add(Restrictions.eq("status", 1));		
		List<Template> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public List<Template> getTemplateById(int templateId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Template.class);
		criteria.add(Restrictions.eq("id", templateId)).add(Restrictions.eq("status", 0));		
		List<Template> list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public int updateTemplate(Template template) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;
		
		try {	
			String sql = "update template SET description = :description,name = :name WHERE id = :id";			
			Query qry = session.createSQLQuery(sql);
			qry.setParameter("description", template.getDescription());
			qry.setParameter("name", template.getName());
			qry.setParameter("id",template.getId());
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
	public int deleteTemplate(Template template) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int temp = 0;		
		try {
			/*Object object = session.load(Template.class,new Integer(template.getId()));
	        Template template2=(Template)object;*/
			Template template2 = new Template();
			template2.setId(template.getId());
			session.delete(template2);
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

}
