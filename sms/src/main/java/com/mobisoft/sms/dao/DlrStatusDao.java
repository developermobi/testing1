package com.mobisoft.sms.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.DlrStatus;

public interface DlrStatusDao {
	public int saveDlrStatus() throws FileNotFoundException, IOException;
	public int sendQuickMessage(List<String> sendQuickListDetails);
}
