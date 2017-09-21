package com.mobisoft.sms.dao;

import java.util.List;

import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

public interface UserReportDao {
	public List todayCountMessage(int userId);
	public List weeklyCountMessage(int userId);
	public List monthlyCountMessage(int userId);
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max);
	public List<Integer> messageCountDaily(int userId,String date);
	public int  messageCountScheduale(int userId);
	public List archiveReportByUserId(int userId,String startDate,String endDate);
	
	public List<UserJobs> scheduleReportByUserId(int userId,int start,int max);
	
	
}
