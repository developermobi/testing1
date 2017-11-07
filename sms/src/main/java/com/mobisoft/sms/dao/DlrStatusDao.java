package com.mobisoft.sms.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;

public interface DlrStatusDao {
	public int saveDlrStatus(List<UserJobs> list) throws FileNotFoundException, IOException;
	public int saveQuickMessage(Map<String,Object> sendQuickListDetails,Session session);
	public List<UserJobs> userJobsCheck(int jobStatus, int schedualStatus);
	public List<UserJobs> userJobsCheckSchedule(int jobStatus, int schedualStatus,Date dateTime);
	
	public List<UserJobs> userJobsCheckPersonalized(int jobStatus, int jobType);
	public int savePersonalizedDlrStatus(List<UserJobs> list) throws FileNotFoundException, IOException;
}
