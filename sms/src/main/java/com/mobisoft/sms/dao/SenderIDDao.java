package com.mobisoft.sms.dao;

import java.util.List;

import com.mobisoft.sms.model.SenderId;
import com.mobisoft.sms.model.Template;

public interface SenderIDDao {

	public int saveSenderId(SenderId senderId);
	
	public List<SenderId> getSenderId();
	
	public List<SenderId> getSenderIdByUserId(int userId);
	
	public List<SenderId> getSenderId(int senderId);	
	
	public int updateSenderId(SenderId senderId);
	
	public int deleteSenderId(SenderId senderId);
}
