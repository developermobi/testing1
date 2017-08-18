package com.mobisoft.sms.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.mobisoft.sms.model.DlrStatus;

public interface DlrStatusDao {
	public int saveDlrStatus(DlrStatus dlrStatus) throws FileNotFoundException, IOException;
}
