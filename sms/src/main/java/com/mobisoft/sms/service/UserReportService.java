package com.mobisoft.sms.service;

import java.util.List;

import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

public interface UserReportService {
	public List todayCountMessage(int userId);
	public List weeklyCountMessage(int userId);
	public List monthlyCountMessage(int userId);
	
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max);
	
	public List archiveReportByUserId(int userId,String startDate,String endDate);
	
	public List<UserJobs> scheduleReportByUserId(int userId,int start,int max);
}
