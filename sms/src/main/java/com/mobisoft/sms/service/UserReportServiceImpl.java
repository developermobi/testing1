package com.mobisoft.sms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mobisoft.sms.dao.UserReportDao;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

@Service
public class UserReportServiceImpl implements UserReportService{
	@Autowired
	private UserReportDao userReportDao;

	@SuppressWarnings("rawtypes")
	@Override
	public List todayCountMessage(int userId) {		
		return userReportDao.todayCountMessage(userId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List weeklyCountMessage(int userId) {		
		return userReportDao.weeklyCountMessage(userId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List monthlyCountMessage(int userId) {		
		return userReportDao.monthlyCountMessage(userId);
	}

	@Override
	public List<DlrStatus> dailyRepotMessage(int userId,String date,String mobile,int start, int max) {		
		return userReportDao.dailyRepotMessage(userId,date,mobile,start,max);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List archiveReportByUserId(int userId,String startDate,String endDate) {
		return userReportDao.archiveReportByUserId(userId,startDate,endDate);
	}

	@Override
	public List<UserJobs> scheduleReportByUserId(int userId,String fromDate,String toDate, int start, int max) {		
		return userReportDao.scheduleReportByUserId(userId,fromDate,toDate, start, max);
	}

	@Override
	public List<Integer> messageCountDaily(int userId, String date) {
		return userReportDao.messageCountDaily(userId, date);
	}

	@Override
	public int messageCountScheduale(int userId,String fromDate,String toDate) {		
		return userReportDao.messageCountScheduale(userId,fromDate,toDate);
	}

	@Override
	public List<UserJobs> compaignStatus(int userId, int start, int max,String fromdate,String toDate) {		
		return userReportDao.compaignStatus(userId, start, max,fromdate,toDate);
	}

	@Override
	public List<UserJobs> compaignStatusCount(int userId,String fromDate,String toDate) {		
		return userReportDao.compaignStatusCount(userId,fromDate,toDate);
	}

	@Override
	public List<DlrStatus> dlrStatusGroupBy(int jobId, int userId) {
		
		return userReportDao.dlrStatusGroupBy(jobId, userId);
	}

	@Override
	public List dlrStausRepotExportDetails(int userId, int jobId, String status) {
		return userReportDao.dlrStausRepotExportDetails(userId, jobId, status);
	}

	@Override
	public Map<Integer, List<DlrStatus>> dlrReportDetails(int userId, int jobId, String status,int start,int max) {
		
		return userReportDao.dlrReportDetails(userId, jobId, status,start,max);
	}

	@Override
	public List<DlrStatus> searchMobileStatus(String moblieNumber, String date) {
	
		return userReportDao.searchMobileStatus(moblieNumber, date);
	}
}
