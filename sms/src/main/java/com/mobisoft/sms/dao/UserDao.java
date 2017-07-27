package com.mobisoft.sms.dao;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;

public interface UserDao {
	
	public int saveUser(User user);
	
	public int saveUerDeatils(JsonNode jsonNode);
	
	//public int saveBalance(SmsBalance smsBalance);
	
	public List<User> getUser();
	
	public List<User> getUserById(int userId);
	
	public List<User> getUserByUserName(String userName);
	
	public int updateUser(User user);
	
	public int deleteUser(User user);
}
