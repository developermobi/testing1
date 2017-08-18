package com.mobisoft.sms.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.mobisoft.sms.model.DlrStatus;

public interface DlrStatusService {
	public int saveDlrStatus(DlrStatus dlrStatus) throws FileNotFoundException, IOException;
}
