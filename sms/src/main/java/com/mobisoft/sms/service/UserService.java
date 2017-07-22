package com.mobisoft.sms.service;

import java.util.List;

import com.mobisoft.sms.model.User;

public interface UserService {
	
	public int saveUser(User user);
	
	public List<User> getUser();
	
	public List<User> getUserById(int adminId);
	
	public int updateUser(User admin);
	
	public int deleteUser(User admin);
}
