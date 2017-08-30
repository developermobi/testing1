package com.mobisoft.sms.dao;

import com.mobisoft.sms.model.UserJobs;

public interface UserJobsDao {
	public int saveUserJobs(UserJobs userJobs,int productId,int sentMessageBalance,int updateNewBalance);
	public int saveUserGroupJobs(UserJobs userJobs,int productId,int sentMessageBalance,int updateNewBalance);
}
