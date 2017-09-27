package com.mobisoft.sms.service;

import java.util.List;
import java.util.Map;

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
	
	public List<User> getUserByResellerId(int resellerId);
	
	public List<User> getUser();
	
	public List<User> getUserById(int userId);
	
	public List<User> getUserByUserName(String userName);
	
	public int updateUser(User user);
	
	public int deleteUser(int userId,int resellerId);
	
	public int addCreditUser(int userId,int resellerId,int productId,int balance);
	
	public int addProdcut(int reselerId,int userId,int prodcutId,int balance);
	
	public int deductCreditUser(int deductUserId,int creditByUserId,int productId,int balance);
	
	public List<User> validateUserName(String userName);
	
	public int changePassword(String oldPassword, String newPassword,int userId);
	
	public  Map<Integer,Integer>  countTransactionList(int userId,int type,int productType);
	
	public Map<Integer, List> transactionList(int userId,int type,int productType, int start,int limt);
	
	
}
