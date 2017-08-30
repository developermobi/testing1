package com.mobisoft.sms.service;

import com.mobisoft.sms.model.UserJobs;

public interface UserJobsService {
	public int saveUserJobs(UserJobs userJobs,int productId,int sentMessageBalance,int updateNewBalance);
	public int saveUserGroupJobs(UserJobs userJobs,int productId,int sentMessageBalance,int updateNewBalance);
}
