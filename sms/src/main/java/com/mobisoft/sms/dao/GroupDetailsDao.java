package com.mobisoft.sms.dao;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.User;

public interface GroupDetailsDao {
	public int saveGroupDetails(JsonNode node);
	public List<GroupDetails> getGroupDetailsByUserId(int userId,int start, int limit);
	public List<GroupDetails> getGroupDetailsCountByUserId(int userId);
	public List<GroupDetails> getGroupDetailsByGroupId(int groupId);
	public List<GroupDetails> getActiveGroupDetailsByUserId(int userId);
	public int updateGroupDetails(GroupDetails groupDetails,int groupId);
	public int deleteGroupDetailsByGroupId(int groupId);
}
