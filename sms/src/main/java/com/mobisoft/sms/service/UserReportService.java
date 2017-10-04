package com.mobisoft.sms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

public interface UserReportService {
	public List todayCountMessage(int userId);
	public List weeklyCountMessage(int userId);
	public List monthlyCountMessage(int userId);
	
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max);
	
	public List<Integer> messageCountDaily(int userId,String date);
	public int  messageCountScheduale(int userId);
	
	public List archiveReportByUserId(int userId,String startDate,String endDate);
	
	public List<UserJobs> scheduleReportByUserId(int userId,int start,int max);
	
	public List<UserJobs> compaignStatus(int userId,int start, int max);
	public List<UserJobs> compaignStatusCount(int userId);
	
	public List<DlrStatus> dlrStatusGroupBy(int jobId,int userId); 
	
	public List dlrStausRepotExportDetails(int userId,int jobId,String status);
}
