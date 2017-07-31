package com.mobisoft.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.GroupDetailsDao;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;

@Service
public class GroupDetailsServiceImpl implements GroupDetailsService{

	@Autowired
	GroupDetailsDao groupDetailsDao;

	@Override
	public int saveGroupDetails(GroupDetails groupDetails,User userGroupDetails) {		
		return groupDetailsDao.saveGroupDetails(groupDetails,userGroupDetails);
	}
}
