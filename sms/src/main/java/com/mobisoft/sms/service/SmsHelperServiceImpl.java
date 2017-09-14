package com.mobisoft.sms.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobisoft.sms.dao.SmsHelperDao;
import com.mobisoft.sms.model.Route;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.SmsDnd;
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserProduct;

@Service
public class SmsHelperServiceImpl implements SmsHelperService{

	@Autowired
	SmsHelperDao smsHelperDao;

	@Override
	public List<Integer> getBalance(int userId, int productId) {		
		return smsHelperDao.getBalance(userId, productId);
	}

	@Override
	public int deductBalanceDeleteUser(int userId,  int resellerId, String remark,
			int deductType,Session session,Transaction tx) {
		return smsHelperDao.deductBalanceDeleteUser(userId,  resellerId,  remark, deductType,session,tx);
	}

	@Override
	public int updateUserBalance(int newBalance, int userId, int productId, Session session, Transaction tx) {		
		return smsHelperDao.updateUserBalance(newBalance, userId, productId, session, tx);
	}

	@Override
	public int creditBalance(int userId, int productId, int resellerId, int balance, String remark, int creditType,
			Session session, Transaction tx) {
		return smsHelperDao.creditBalance(userId, productId, resellerId, balance, remark, creditType, session, tx);
	}

	@Override
	public int debitBalnce(int userId, int productId, int resellerId, int balance, String remark, int debitType,
			Session session, Transaction tx) {
		return smsHelperDao.debitBalnce(userId, productId, resellerId, balance, remark, debitType, session, tx);
	}

	@Override
	public List<SmsBalance> findProdcut(int userId, int prodcutId) {		
		return smsHelperDao.findProdcut(userId, prodcutId);
	}

	@Override
	public int messageCount(int messageType, int messageLenght) {		
		return smsHelperDao.messageCount(messageType, messageLenght);
	}

	@Override
	@Transactional("txManager2")
	public List<SmsDnd> filterDndNumber() {
		
		return smsHelperDao.filterDndNumber();
	}

	@Override
	public List<UserProduct> getRouteDetails(int userId, int productId) {
		
		return smsHelperDao.getRouteDetails(userId, productId);
	}

	@Override
	public List<String> getGroupContact(String groupId, int userId) {
		
		return smsHelperDao.getGroupContact(groupId, userId);
	}

	@Override
	public List<UserAuthrization> getUserAuthrizationCheck(int userId) {
		
		return smsHelperDao.getUserAuthrizationCheck(userId);
	}

	@Override
	public List<Object> mobileNumber(String mobileNumber) {
		
		return smsHelperDao.mobileNumber(mobileNumber);
	}
}
