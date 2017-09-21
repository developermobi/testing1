package com.mobisoft.sms.restcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.Contact;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.model.UserProduct;
import com.mobisoft.sms.service.ContactService;
import com.mobisoft.sms.utility.TokenAuthentication;

import au.com.bytecode.opencsv.CSVReader;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class ContactRestController {
	
	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private ContactService contactService;
	
	private ObjectMapper mapper = null;
	
	@Value("${uploadUserContact}")
	private String uploadUserContact;
	
	private BufferedReader br = null;
	
	private FileReader fr = null;
	
	private File userContactFile;
	
	private String fileName;
	
	private List<String> mobileList;
	
	String rootPath = System.getProperty("catalina.home");
	
	@RequestMapping(value = "/saveContact",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> saveGroupDetails(@RequestHeader("Authorization") String authorization,@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{

		Map<String,Object> map = new HashMap<>();		
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 401);
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
				map.put("code", 403);
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
			 dataMap.put("total", contactCount);
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
			
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else{	
			int status = 2;
			int result = contactService.updateContactStatusByContactId(contactId,status);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 200);
				map.put("message", "deleted successfully");
				map.put("data", result);
			}else{
				map.put("status", "error");
				map.put("code", 304);
				map.put("message", "error occured during detetion");
				map.put("data", result);
			}
		}
	
		return map;	
	}
	@RequestMapping( value = "/updateContactStatus/{contactId}/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> updateContactStatus(@PathVariable("contactId") int contactId,@PathVariable("status") int status,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else{	
			int result = contactService.updateContactStatusByContactId(contactId,status);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 200);
				map.put("message", "update successfully");
				map.put("data", result);
			}else{
				map.put("status", "error");
				map.put("code", 304);
				map.put("message", "error occured during updation");
				map.put("data", result);
			}
		}
	
		return map;	
	}
	@RequestMapping(value="/saveMultipleContact",method = RequestMethod.POST)
	public Map<String,Object>saveUserJobs(@RequestHeader("Authorization") String authorization,@RequestParam("contactFile")MultipartFile multipartFile,
			@RequestParam("userId")int userId,
			@RequestParam("groupId")int groupId
			) throws IllegalStateException, ParseException, IOException{

		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1){
			
			String fileName = "";
			if(!multipartFile.isEmpty()){
				
			    try {
			    	
			    	fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			    	String newFileName = userId+fileName;
			    	System.out.println("file size:- "+multipartFile.getSize());
			    	if(multipartFile.getSize() <= 3000000 )
			    	{
			    		
			    		if(multipartFile.getOriginalFilename().endsWith(".csv")){
			    			
							String fileUploadDirectory =  uploadUserContact+"/";						
							userContactFile = new File(fileUploadDirectory);
							System.out.println("inside if directory");
					        if (!userContactFile.exists()) {
					            if (!userContactFile.mkdirs()) {
					            	
					            	map.put("code", 404);
					    			map.put("status", "error");
					    			map.put("message", "file Upload Directory has not found");
					            }
					        }
					        userContactFile = new File(fileUploadDirectory,newFileName);
							multipartFile.transferTo(userContactFile);
							System.out.println(userContactFile.getAbsolutePath());
							CSVReader reader = new CSVReader(new FileReader(userContactFile));
							int result = contactService.uploadMultipleContact(groupId, userId, reader);
							if(result == 1){
								map.put("status", "success");
								map.put("code", 200);
								map.put("message", "Successfully Add Contact");
								map.put("data", result);
							}else if(result == 2 || result == 0){
								map.put("status", "error");
								map.put("code", 400);
								map.put("message", "error occured during Insert Contact");
								map.put("data", result);
							}
					       /* String [] nextLine;
					        while ((nextLine = reader.readNext()) != null) {
					            // nextLine[] is an array of values from the line
					            System.out.println(nextLine[0]+"  "+nextLine[1]+"  "+nextLine[2]+"  "+nextLine[3]);					           
					        }*/

						}
			    		else
			    		{
			    			map.put("code", 413);
							map.put("status", "error");
							map.put("message", "Invalid file format please select csv file....");	
			    		}

			    	}
			    	else
			    	{
			    		map.put("code", 413);
						map.put("status", "error");
						map.put("message", "Request File Too Large");	
			    	}
			    	

				} catch (IOException e) {
					map.put("code", 404);
	    			map.put("status", e.getMessage());
	    			map.put("message", "File Not Upload");
				}
			}
		}
		else
		{
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");	
		}
		return map;
		
		
	
	}
	//Dashboard report part
	@RequestMapping(value = "/getAllContactCount/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllContactByUserId(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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

			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("total", contactCount.size());
			
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

}
