package com.mobisoft.sms.service;

import java.util.List;

import com.mobisoft.sms.model.SenderId;

public interface SenderIdService {

public int saveSenderId(SenderId senderId);
	
	public List<SenderId> getSenderId();
	
	public List<SenderId> getSenderIdByUserId(int userId);
	
	public List<SenderId> getSenderIdByUserIdPaginate(int userId,int start,int limit);
	
	public List<SenderId> getSenderId(int senderId);	
	
	public int updateSenderId(SenderId senderId);
	
	public int deleteSenderId(SenderId senderId);
}
