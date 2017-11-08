package com.mobisoft.sms.dao;

import java.util.List;

import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Contact;

import au.com.bytecode.opencsv.CSVReader;

public interface ContactDao {
	public int saveConact(JsonNode node);
	public List<Contact> getContactByUserId(int userId,int start, int limit);
	public List<Contact> getContactCountByUserId(int userId);
	public List<Contact> getContactCountByGroupId(int groupId,int start,int max);
	public List<Contact> getContactByContactId(int contactId);
	public int updateContact(JsonNode node,int contactId);
	public int updateContactStatusByContactId(int contactId, int status);
	public int uploadMultipleContact(int groupId,final int userId,final CSVReader reader);
	public List<Contact> countGroupConatct(int groupId);
	@SuppressWarnings("rawtypes")
	public List getGroupConatct(int groupId,Session session);
}
