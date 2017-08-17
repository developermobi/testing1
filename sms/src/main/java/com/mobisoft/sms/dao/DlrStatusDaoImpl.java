package com.mobisoft.sms.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println(mobileList.size());
		} catch (Exception e) {
			
		}finally{
			
		}
		
		
		System.out.println(list.size());
		session.close();
		return 0;
	}

}
