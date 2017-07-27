package com.mobisoft.sms.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.UserDao;
import com.mobisoft.sms.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userDao;
	public int saveUser(User user) {
		
		return userDao.saveUser(user);
	}
	@Override
	public List<User> getUser() {
		return userDao.getUser();
	}
	@Override
	public List<User> getUserById(int adminId) {
		return userDao.getUserById(adminId);
	}
	@Override
	public int updateUser(User admin) {
		return userDao.updateUser(admin);
	}
	@Override
	public int deleteUser(User admin) {
		return userDao.deleteUser(admin);
	}
	@Override
	public List<User> getUserByUserName(String userName) {
		
		return userDao.getUserByUserName(userName);
	}
	@Override
	public int saveUerDeatils(JsonNode jsonNode) {
		
		return userDao.saveUerDeatils(jsonNode);
	}

}
