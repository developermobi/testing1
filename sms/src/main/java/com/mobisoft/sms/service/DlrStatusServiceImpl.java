package com.mobisoft.sms.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.DlrStatusDao;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

@Service
public class DlrStatusServiceImpl implements DlrStatusService{

	@Autowired
	private DlrStatusDao dlrStatusDao;
	
	@Override
	public int saveDlrStatus(List<UserJobs> list) throws FileNotFoundException, IOException {
		
		return dlrStatusDao.saveDlrStatus(list);
	}

	@Override
	public int saveQuickMessage(Map<String, Object> sendQuickListDetails,Session session) {
	
		return dlrStatusDao.saveQuickMessage(sendQuickListDetails,session);
	}

	@Override
	public List<UserJobs> userJobsCheck(int jobStatus, int schedualStatus) {
		
		return dlrStatusDao.userJobsCheck(jobStatus, schedualStatus);
	}

	@Override
	public List<UserJobs> userJobsCheckSchedule(int jobStatus, int schedualStatus, Date dateTime) {
		return dlrStatusDao.userJobsCheckSchedule(jobStatus, schedualStatus, dateTime);
	}

	@Override
	public List<UserJobs> userJobsCheckPersonalized(int jobStatus, int jobType) {
		return dlrStatusDao.userJobsCheckPersonalized(jobStatus, jobType);
	}

	@Override
	public int savePersonalizedDlrStatus(List<UserJobs> list) throws FileNotFoundException, IOException {
		return dlrStatusDao.savePersonalizedDlrStatus(list);
	}

}
