package com.mobisoft.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.UserJobsDao;
import com.mobisoft.sms.model.UserJobs;

@Service
public class UserJobsServiceImpl implements UserJobsService{
	@Autowired
	UserJobsDao userJobsDao;

	@Override
	public int saveUserJobs(UserJobs userJobs) {
		
		return userJobsDao.saveUserJobs(userJobs);
	}
}
