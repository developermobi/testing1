package com.mobisoft.sms.dao;

import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;

public interface GroupDetailsDao {
	public int saveGroupDetails(GroupDetails groupDetails,User userGroupDetails);
}
