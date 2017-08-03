package com.mobisoft.sms.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;

public interface UserService {
	
	public int saveUser(User user);
	
	public int saveUerDeatils(JsonNode jsonNode);
	
	public List<SmsBalance> getBalanceByUserId(int userId);
	
	public List<Credit> getCreditDetailsByUserId(int userId);
	
	public List<Debit> getDebitByUserId(int userId);
	
	public List<User> getUser();
	
	public List<User> getUserById(int userId);
	
	public List<User> getUserByUserName(String userName);
	
	public int updateUser(User user);
	
	public int deleteUser(User user);
}
