package com.mobisoft.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.SenderIDDao;
import com.mobisoft.sms.model.SenderId;

@Service
public class SenderIdServiceImpl implements SenderIdService{

	@Autowired
	SenderIDDao senderIdDao;
	@Override
	public int saveSenderId(SenderId senderId) {
		
		return senderIdDao.saveSenderId(senderId);
	}

	@Override
	public List<SenderId> getSenderId() {
		
		return senderIdDao.getSenderId();
	}

	@Override
	public List<SenderId> getSenderIdByUserId(int userId) {
		
		return senderIdDao.getSenderIdByUserId(userId);
	}

	@Override
	public List<SenderId> getSenderId(int senderId) {
		
		return senderIdDao.getSenderId(senderId);
	}

	@Override
	public int updateSenderId(SenderId senderId) {
		
		return senderIdDao.updateSenderId(senderId);
	}

	@Override
	public int deleteSenderId(SenderId senderId) {
		
		return senderIdDao.deleteSenderId(senderId);
	}

	@Override
	public List<SenderId> getSenderIdByUserIdPaginate(int userId, int start, int limit) {
		return senderIdDao.getSenderIdByUserIdPaginate(userId, start, limit);
	}

}
