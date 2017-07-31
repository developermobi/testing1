package com.mobisoft.sms.service;

import java.util.List;

import com.mobisoft.sms.model.SmsBalance;

public interface SmsHelperService {

	public List<Integer> getBalance(int userId,int productId);
}
