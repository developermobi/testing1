package com.mobisoft.sms.dao;

import java.util.List;

import com.mobisoft.sms.model.DlrStatus;

public interface UserReportDao {
	public List todayCountMessage(int userId);
	public List weeklyCountMessage(int userId);
	public List monthlyCountMessage(int userId);
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max);
	
	public List<DlrStatus> archiveReportByUserId(int userId);
}
