package com.mobisoft.sms.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.DlrStatusDao;
import com.mobisoft.sms.model.DlrStatus;

@Service
public class DlrStatusServiceImpl implements DlrStatusService{

	@Autowired
	DlrStatusDao dlrStatusDao;
	
	@Override
	public int saveDlrStatus(DlrStatus dlrStatus) throws FileNotFoundException, IOException {
		
		return dlrStatusDao.saveDlrStatus(dlrStatus);
	}

}
