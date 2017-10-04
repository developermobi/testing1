package com.mobisoft.sms.service;

import java.util.List;
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
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max) {		
		return userReportDao.dailyRepotMessage(userId,start,max);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List archiveReportByUserId(int userId,String startDate,String endDate) {
		return userReportDao.archiveReportByUserId(userId,startDate,endDate);
	}

	@Override
	public List<UserJobs> scheduleReportByUserId(int userId, int start, int max) {		
		return userReportDao.scheduleReportByUserId(userId, start, max);
	}

	@Override
	public List<Integer> messageCountDaily(int userId, String date) {
		return userReportDao.messageCountDaily(userId, date);
	}

	@Override
	public int messageCountScheduale(int userId) {		
		return userReportDao.messageCountScheduale(userId);
	}

	@Override
	public List<UserJobs> compaignStatus(int userId, int start, int max) {		
		return userReportDao.compaignStatus(userId, start, max);
	}

	@Override
	public List<UserJobs> compaignStatusCount(int userId) {		
		return userReportDao.compaignStatusCount(userId);
	}

	@Override
	public List<DlrStatus> dlrStatusGroupBy(int jobId, int userId) {
		
		return userReportDao.dlrStatusGroupBy(jobId, userId);
	}
}
