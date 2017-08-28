package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.Contact;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.service.ContactService;
import com.mobisoft.sms.utility.TokenAuthentication;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class ContactRestController {
	
	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private ContactService contactService;
	
	private ObjectMapper mapper = null;
	
	@RequestMapping(value = "/saveContact",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> saveGroupDetails(@RequestHeader("Authorization") String authorization,@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{

		Map<String,Object> map = new HashMap<>();		
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else
		{
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);

			int result = contactService.saveConact(node);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 201);
				map.put("message", "Successfully Add Contact");
				map.put("data", result);
			}else{
				map.put("status", "error");
				map.put("code", 400);
				map.put("message", "error occured during insertion");
				map.put("data", result);
			}
			
		}
		
		return map;
	}
	@RequestMapping(value = "/getAllContact/{userId}/{start}/{limit}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllContact(@PathVariable("userId")int userId,@PathVariable("start")int start,@PathVariable("limit")int limit,@RequestHeader("Authorization") String authorization)
	{
		System.out.println(userId);
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		if(tokenAuthentication.validateToken(authorization) == 0){
					
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else {
			List<Contact> contactCount = contactService.getContactCountByUserId(userId);
			List<Contact> contact = contactService.getContactByUserId(userId, start, limit);
			
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("total", contactCount.size());
			 dataMap.put("contactData", contact);
			//System.out.println("get Data :-- " + alltemplateList.get(4).getDescription());
			if(contactCount.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "Data found");
				map.put("data", dataMap);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", dataMap);
			}
		}		
		
		return map;
	}
	@RequestMapping(value = "getContactById/{contactId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getContactById(@PathVariable("contactId")int contactId,@RequestHeader("Authorization") String authorization)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);		
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			List<Contact> groupList = contactService.getContactByContactId(contactId);
			
			if(groupList.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", groupList);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", groupList);
			}
		}
		
		return map;
	}
	@RequestMapping(value = "updateContactById/{contactId}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>updateContactById(@PathVariable("contactId")int contactId,@RequestBody String josnString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
	{
		Map<String,Object> map = new HashMap<>();
		
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else{

			mapper = new ObjectMapper();		
			JsonNode node = mapper.readValue(josnString, JsonNode.class);
			int result = contactService.updateContact(node, contactId);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 200);
				map.put("message", "updated successfully");
				map.put("data", result);
			}else{
				map.put("status", "error");
				map.put("code", 400);
				map.put("message", "error occured during updation");
				map.put("data", result);
			}
		}

		return map;
		
	}
	@RequestMapping( value = "/deleteContact/{contactId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deleteContact(@PathVariable("contactId") int contactId,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else{	
			int result = contactService.deleteContactByContactId(contactId);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 200);
				map.put("message", "deleted successfully");
				map.put("data", result);
			}else{
				map.put("status", "error");
				map.put("code", 400);
				map.put("message", "error occured during detetion");
				map.put("data", result);
			}
		}
	
		return map;	
	}

}
