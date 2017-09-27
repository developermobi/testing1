package com.mobisoft.sms.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.DlrStatusDao;
import com.mobisoft.sms.model.DlrStatus;

@Service
public class DlrStatusServiceImpl implements DlrStatusService{

	@Autowired
	private DlrStatusDao dlrStatusDao;
	
	@Override
	public int saveDlrStatus() throws FileNotFoundException, IOException {
		
		return dlrStatusDao.saveDlrStatus();
	}

	@Override
	public int saveQuickMessage(Map<String, Object> sendQuickListDetails,Session session) {
	
		return dlrStatusDao.saveQuickMessage(sendQuickListDetails,session);
	}

}
