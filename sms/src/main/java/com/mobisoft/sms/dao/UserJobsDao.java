package com.mobisoft.sms.dao;

import java.util.Map;

import com.mobisoft.sms.model.UserJobs;

public interface UserJobsDao {
	public int saveUserJobs(UserJobs userJobs,int productId,int sentMessageBalance,int updateNewBalance);
	public int sendQuickMessage(Map<String,Object> mapList);
}
