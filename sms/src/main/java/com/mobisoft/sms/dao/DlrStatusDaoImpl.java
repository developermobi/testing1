package com.mobisoft.sms.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.model.UserJobs;

@Repository("dlrStatusDao")
public class DlrStatusDaoImpl implements DlrStatusDao{

	
	@Autowired
	SessionFactory sessionFactory;
	
	@Value("${uploadUserTextFile}")
	private String uploadUserTextFile;
	
	Session session = null;
	Transaction tx = null;
	File file;
	FileReader fr = null;
	BufferedReader br = null;
	
	@Override
	public int saveDlrStatus(DlrStatus dlrStatus) {
		
		session = sessionFactory.openSession();
		tx= session.beginTransaction();
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserJobs.class);
		criteria.add(Restrictions.eq("jobStatus", 0));
		List<UserJobs> list = criteria.list();
		String fileName = list.get(0).getFilename();
		try {
			file = new File(uploadUserTextFile,fileName);
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line="";
			List<String> mobileList = new ArrayList<>();
			while((line = br.readLine()) != null)
			{
				mobileList.add(line);
				
			}
			for (int i = 0; i < mobileList.size(); i++) {
				org.hibernate.Query query = session.createSQLQuery("CALL spInsert(:job_id,:Sender,:momt,:type,:mclass,:coding,:message,:mobile,:provider_id,:user_id,:account)")
						  .addEntity(DlrStatus.class)
						  .setParameter("job_id",list.get(0).getId())
						  .setParameter("Sender",list.get(0).getSender())
						  .setParameter("momt", "MO")
						  .setParameter("type", 1)
						  .setParameter("mclass", 1)
						  .setParameter("coding", 0)

						  .setParameter("message",list.get(0).getMessage())

						  .setParameter("mobile",mobileList.get(0))
						  .setParameter("provider_id","New Foo")

						  .setParameter("user_id",Integer.toString(list.get(0).getUserId()))
						  .setParameter("account",Integer.toString(list.get(0).getUserId()));
				query.executeUpdate();
				if( i % 50 == 0 ) { // Same as the JDBC batch size
					
			        session.flush();
			        session.clear();
				}
			}
			
			System.out.println("aFTER pROCEDURES"+mobileList.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			
		}
		
		
		System.out.println(list.size());
		session.close();
		return 0;
	}

}
