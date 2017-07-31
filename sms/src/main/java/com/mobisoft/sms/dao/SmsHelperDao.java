package com.mobisoft.sms.dao;

import java.util.List;

import com.mobisoft.sms.model.SmsBalance;


public interface SmsHelperDao {
	public List<Integer> getBalance(int userId,int productId);
}
