package com.mobisoft.sms.dao;

import java.util.List;

import com.mobisoft.sms.model.User;

public interface UserDao {
	
	public int saveUser(User user);
	
	public List<User> getUser();
	
	public List<User> getAdminById(int adminId);
	
	public int updateAdmin(User admin);
	
	public int deleteAdmin(User admin);
}
