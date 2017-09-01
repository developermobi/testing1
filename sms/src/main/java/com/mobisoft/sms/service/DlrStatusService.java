package com.mobisoft.sms.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.hibernate.Session;

import com.mobisoft.sms.model.DlrStatus;

public interface DlrStatusService {
	public int saveDlrStatus() throws FileNotFoundException, IOException;
	public int saveQuickMessage(Map<String,Object> sendQuickListDetails,Session session);
}
