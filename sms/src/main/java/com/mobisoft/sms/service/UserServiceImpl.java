package com.mobisoft.sms.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.UserDao;
import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
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
	public int deleteUser(int userId,int resellerId) {
		return userDao.deleteUser(userId,resellerId);
	}
	@Override
	public List<User> getUserByUserName(String userName) {
		return userDao.getUserByUserName(userName);
	}
	@Override
	public int saveUerDeatils(JsonNode jsonNode) {
		return userDao.saveUerDeatils(jsonNode);
	}
	@Override
	public List<SmsBalance> getBalanceByUserId(int userId) {
		return userDao.getBalanceByUserId(userId);
	}
	@Override
	public List<Credit> getCreditDetailsByUserId(int userId) {
		return userDao.getCreditDetailsByUserId(userId);
	}
	@Override
	public List<Debit> getDebitByUserId(int userId) {
		return userDao.getDebitByUserId(userId);
	}
	@Override
	public Map<Integer,List<User>> getUserByResellerId(int resellerId,int start,int max) {
		return userDao.getUserByResellerId(resellerId,start,max);
	}
	@Override
	public int addCreditUser(int userId, int resellerId, int productId, int balance) {
		return userDao.addCreditUser(userId, resellerId, productId, balance);
	}
	@Override
	public int addProdcut(int reselerId, int userId, int prodcutId, int balance) {		
		return userDao.addProdcut(reselerId, userId, prodcutId, balance);
	}
	@Override
	public int deductCreditUser(int deductUserId, int creditByUserId, int productId, int balance) {
		return userDao.deductCreditUser(deductUserId, creditByUserId, productId, balance);
	}
	@Override
	public List<User> validateUserName(String userName) {
		
		return userDao.validateUserName(userName);
	}
	@Override
	public int changePassword(String oldPassword, String newPassword, int userId) {
	
		return userDao.changePassword(oldPassword, newPassword, userId);
	}
	@Override
	public Map<Integer, List> countTransactionList(int userId, int type, int productType) {
		
		return userDao.countTransactionList(userId, type, productType);
	}
	@Override
	public List transactionList(int userId, int type, int productType, int start, int limt) {
	
		return userDao.transactionList(userId, type, productType, start, limt);
	}

}
