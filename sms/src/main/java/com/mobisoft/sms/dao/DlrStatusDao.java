package com.mobisoft.sms.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.DlrStatus;

public interface DlrStatusDao {
	public int saveDlrStatus() throws FileNotFoundException, IOException;
	public int saveQuickMessage(Map<String,Object> sendQuickListDetails,Session session);
}
