package com.mobisoft.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.dao.GroupDetailsDao;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;

@Service
public class GroupDetailsServiceImpl implements GroupDetailsService{

	@Autowired
	private GroupDetailsDao groupDetailsDao;

	@Override
	public int saveGroupDetails(JsonNode node) {		
		return groupDetailsDao.saveGroupDetails(node);
	}

	@Override
	public List<GroupDetails> getGroupDetailsByUserId(int userId,int start, int limit) {
		
		return groupDetailsDao.getGroupDetailsByUserId(userId,start,limit);
	}

	@Override
	public List<GroupDetails> getGroupDetailsByGroupId(int groupId) {
		
		return groupDetailsDao.getGroupDetailsByGroupId(groupId);
	}

	@Override
	public int updateGroupDetails(GroupDetails groupDetails,int groupId) {
		
		return groupDetailsDao.updateGroupDetails(groupDetails,groupId);
	}

	@Override
	public int deleteGroupDetailsByGroupId(int groupId) {
		
		return groupDetailsDao.deleteGroupDetailsByGroupId(groupId);
	}

	@Override
	public List<GroupDetails> getGroupDetailsCountByUserId(int userId) {
		return groupDetailsDao.getGroupDetailsCountByUserId(userId);
	}

	@Override
	public List<GroupDetails> getActiveGroupDetailsByUserId(int userId) {
		return groupDetailsDao.getActiveGroupDetailsByUserId(userId);
	}
}
