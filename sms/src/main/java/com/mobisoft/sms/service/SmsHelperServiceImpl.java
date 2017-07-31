package com.mobisoft.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.SmsHelperDao;
import com.mobisoft.sms.model.SmsBalance;


@Service
public class SmsHelperServiceImpl implements SmsHelperService{

	@Autowired
	SmsHelperDao smsHelperDao;

	@Override
	public List<Integer> getBalance(int userId, int productId) {
		
		return smsHelperDao.getBalance(userId, productId);
	}
}
