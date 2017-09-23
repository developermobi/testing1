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
	
	Session session = null;
	
	Transaction tx = null;
	
	@Override
	public int saveTemplate(Template template) {
		session =  sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {
			session.saveOrUpdate(template);
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
	public List<Template> getTemplate() {
		session = sessionFactory.openSession();
		List<Template> list = null;
		try {
			list = session.createCriteria(Template.class).list();
			//session.close();
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

	@Override
	public List<Template> getTemplateByUserId(int userId) {
		session = sessionFactory.openSession();
		List<Template> list = null;
		try {
			User user =(User)session.get(User.class, userId);
			Criteria criteria = session.createCriteria(Template.class);
			criteria.add(Restrictions.eq("userId",user))
			.add(Restrictions.eq("status", 0));		
			list = criteria.list();
			//session.close();
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

	@Override
	public List<Template> getTemplateById(int templateId) {
		session = sessionFactory.openSession();
		List<Template> list = null;
		try {
			Criteria criteria = session.createCriteria(Template.class);
			criteria.add(Restrictions.eq("id", templateId)).add(Restrictions.eq("status", 0));		
			list = criteria.list();
			//session.close();
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

	@Override
	public int updateTemplate(Template template) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;
		
		try {	
			String sql = "update template SET description = :description,name = :name WHERE id = :id";			
			Query qry = session.createSQLQuery(sql);
			qry.setParameter("description", template.getDescription());
			qry.setParameter("name", template.getName());
			qry.setParameter("id",template.getId());
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
	public int deleteTemplate(Template template) {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		int temp = 0;		
		try {
			/*Object object = session.load(Template.class,new Integer(template.getId()));
	        Template template2=(Template)object;*/
			Template template2 = new Template();
			template2.setId(template.getId());
			session.delete(template2);
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
	public List<Template> getTemplateByUserIdPaginate(int userId, int start, int limit) {
		session = sessionFactory.openSession();
		List<Template> list = null;
		try {
			User user =(User)session.get(User.class, userId);
			System.out.println(user.getUserId());
			Criteria criteria = session.createCriteria(Template.class);
			criteria.add(Restrictions.eq("userId",user))
			.add(Restrictions.eq("status", 0))
			.setFirstResult(start)
			.setMaxResults(limit);		
			list = criteria.list();
			//session.close();
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
