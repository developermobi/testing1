package com.mobisoft.sms.dao;

import javax.jws.soap.SOAPBinding.Use;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;


@Repository("groupDetailsDao")
public class GroupDetailsDaoImpl implements GroupDetailsDao{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public int saveGroupDetails(GroupDetails groupDetails,User userGroupDetails) {
		Session session =  sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
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

}
