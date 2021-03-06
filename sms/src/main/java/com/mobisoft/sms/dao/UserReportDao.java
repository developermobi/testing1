package com.mobisoft.sms.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

public interface UserReportDao {
	public List todayCountMessage(int userId);
	public List weeklyCountMessage(int userId);
	public List monthlyCountMessage(int userId);
	public List<DlrStatus> dailyRepotMessage(int userId,String date,String mobile, int start, int max);
	public List<Integer> messageCountDaily(int userId,String date);
	
	public List archiveReportByUserId(int userId,String startDate,String endDate);
	
	public List<UserJobs> compaignStatus(int userId,int start, int max,String fromDate,String toDate);
	public List<UserJobs> compaignStatusCount(int userId,String fromDate,String toDate);
	
	public int  messageCountScheduale(int userId,String fromDate,String toDate);
	public List<UserJobs> scheduleReportByUserId(int userId,String fromDate,String toDate, int start,int max);
	
	public List<DlrStatus> dlrStatusGroupBy(int jobId,int userId); 
	
	public List dlrStausRepotExportDetails(int userId,int jobId,String status);
	
	public  Map<Integer,List<DlrStatus>> dlrReportDetails(int userId,int jobId,String status,int start,int max);
	public List<DlrStatus> searchMobileStatus(String moblieNumber,String date);
	
	
	
}
