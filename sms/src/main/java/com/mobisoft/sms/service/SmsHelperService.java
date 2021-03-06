package com.mobisoft.sms.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mobisoft.sms.model.Route;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.SmsDnd;
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserProduct;

public interface SmsHelperService {

	public List<Integer> getBalance(int userId,int productId);
	public int deductBalanceDeleteUser(int userId,int resellerId,String remark,int deductType,Session session,Transaction tx);
	public int updateUserBalance(int newBalance,int userId,int productId,Session session,Transaction tx);
	public int creditBalance(int userId,int productId,int resellerId,int balance,String remark,int creditType,Session session,Transaction tx);
	public int debitBalnce(int userId,int productId,int resellerId,int balance,String remark,int debitType,Session session,Transaction tx);
	public List<SmsBalance> findProdcut(int userId,int prodcutId);
	public int messageCount(int messageType,int messageLenght);
	/*public List<SmsDnd> filterDndNumber();*/
	public List<UserProduct>getRouteDetails(int userId,int productId);
	public List<String>getGroupContact(String groupId,int userId);
	public List<UserAuthrization> getUserAuthrizationCheck(int userId,int productId);
	public List<Object> mobileNumber(String mobileNumber);
	public int genrateOtp(int userId);
	public int varifyOtp(String otp,int userId);
	public String encriptPassword(String password);	
}
