package com.mobisoft.sms.dao;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Contact;
import com.mobisoft.sms.model.GroupDetails;

public interface ContactDao {
	public int saveConact(JsonNode node);
	public List<Contact> getContactByUserId(int userId,int start, int limit);
	public List<Contact> getContactCountByUserId(int userId);
	public List<Contact> getContactByContactId(int contactId);
	public int updateContact(JsonNode node,int contactId);
	public int deleteContactByContactId(int contactId);
}
