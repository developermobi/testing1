package com.mobisoft.sms.service;

import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;

public interface GroupDetailsService {
	public int saveGroupDetails(GroupDetails groupDetails,User userGroupDetails);
}
