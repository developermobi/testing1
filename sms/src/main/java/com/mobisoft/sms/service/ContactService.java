package com.mobisoft.sms.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.model.Contact;

import au.com.bytecode.opencsv.CSVReader;

public interface ContactService {

	public int saveConact(JsonNode node);
	public List<Contact> getContactByUserId(int userId,int start, int limit);
	public List<Contact> getContactCountByUserId(int userId);
	public List<Contact> getContactByContactId(int contactId);
	public List<Contact> getContactCountByGroupId(int groupId,int start,int max);
	public int updateContact(JsonNode node,int contactId);
	public int updateContactStatusByContactId(int contactId, int status);
	public int uploadMultipleContact(int groupId,int userId,CSVReader reader);
	public List<Contact> countGroupConatct(int groupId);
}
