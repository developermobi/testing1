package com.mobisoft.sms.service;

import java.util.Map;

import com.mobisoft.sms.model.UserJobs;

public interface UserJobsService {
	public int saveUserJobs(UserJobs userJobs,int productId,int sentMessageBalance,int updateNewBalance);
	public int sendQuickMessage(Map<String, Object> mapList);
}
