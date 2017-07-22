package com.mobisoft.sms.service;

import java.util.List;

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
		return userDao.getAdminById(adminId);
	}
	@Override
	public int updateUser(User admin) {
		return userDao.updateAdmin(admin);
	}
	@Override
	public int deleteUser(User admin) {
		return userDao.deleteAdmin(admin);
	}

}
