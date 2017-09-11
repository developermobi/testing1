package com.mobisoft.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.UserReportDao;
import com.mobisoft.sms.model.DlrStatus;

@Service
public class UserReportServiceImpl implements UserReportService{
	@Autowired
	UserReportDao userReportDao;

	@Override
	public List todayCountMessage(int userId) {
		
		return userReportDao.todayCountMessage(userId);
	}

	@Override
	public List weeklyCountMessage(int userId) {
		// TODO Auto-generated method stub
		return userReportDao.weeklyCountMessage(userId);
	}

	@Override
	public List monthlyCountMessage(int userId) {
		// TODO Auto-generated method stub
		return userReportDao.monthlyCountMessage(userId);
	}

	@Override
	public List<DlrStatus> dailyRepotMessage(int userId,int start, int max) {
		
		return userReportDao.dailyRepotMessage(userId,start,max);
	}

	@Override
	public List<DlrStatus> archiveReportByUserId(int userId) {
		return userReportDao.archiveReportByUserId(userId);
	}
}
