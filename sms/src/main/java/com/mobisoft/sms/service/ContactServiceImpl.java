package com.mobisoft.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobisoft.sms.dao.ContactDao;
import com.mobisoft.sms.model.Contact;

import au.com.bytecode.opencsv.CSVReader;

@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	ContactDao contactDao;
	
	@Override
	public int saveConact(JsonNode node) {		
		return contactDao.saveConact(node);
	}
	@Override
	public List<Contact> getContactByUserId(int userId, int start, int limit) {
		
		return contactDao.getContactByUserId(userId, start, limit);
	}
	@Override
	public List<Contact> getContactCountByUserId(int userId) {
		
		return contactDao.getContactCountByUserId(userId);
	}
	@Override
	public List<Contact> getContactByContactId(int contactId) {
		
		return contactDao.getContactByContactId(contactId);
	}
	@Override
	public int updateContact(JsonNode node, int contactId) {
		
		return contactDao.updateContact(node, contactId);
	}
	@Override
	public int updateContactStatusByContactId(int contactId, int status) {
		
		return contactDao.updateContactStatusByContactId(contactId,status);
	}
	@Override
	public int uploadMultipleContact(int groupId, int userId, CSVReader reader) {
		
		return contactDao.uploadMultipleContact(groupId, userId, reader);
	}
	@Override
	public List<Contact> getContactCountByGroupId(int groupId,int start,int max) {
		
		return contactDao.getContactCountByGroupId(groupId,start,max);
	}
	@Override
	public List<Contact> countGroupConatct(int groupId) {
		
		return contactDao.countGroupConatct(groupId);
	}

}
