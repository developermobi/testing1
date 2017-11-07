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
import com.mobisoft.sms.service.GroupDetailsService;
import com.mobisoft.sms.service.UserService;
import com.mobisoft.sms.utility.TokenAuthentication;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class GroupDetailsRestController {

	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private GroupDetailsService groupDetailsService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private UserService userService;

	private ObjectMapper mapper = null;
	
	@RequestMapping(value = "/saveGroupDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
			int result = groupDetailsService.saveGroupDetails(node);
				if(result == 1){
					map.put("status", "success");
					map.put("code", 201);
					map.put("message", "Successfully Add Group");
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
	@RequestMapping(value = "/getAllGroupPaginate/{userId}/{start}/{limit}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllGroupPaginate(@PathVariable("userId")int userId,@PathVariable("start")int start,@PathVariable("limit")int limit,@RequestHeader("Authorization") String authorization)
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
			List<GroupDetails> groupDetailsCount = groupDetailsService.getGroupDetailsCountByUserId(userId);
			List<GroupDetails> groupDetails = groupDetailsService.getGroupDetailsByUserId(userId, start, limit);
		
			
			System.out.println("groupDetails: "+groupDetails.get(0));
			
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("total", groupDetailsCount.size());
			 dataMap.put("groupDetailsData", groupDetails);
			//System.out.println("get Data :-- " + alltemplateList.get(4).getDescription());
			if(groupDetailsCount.size() > 0){
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
	@RequestMapping(value = "/getGroupContact/{groupId}/{start}/{limit}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getGroupContact(@PathVariable("groupId")int groupId,@PathVariable("start")int start,@PathVariable("limit")int limit,@RequestHeader("Authorization") String authorization)
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
		else {
			List<Contact> listContactByGroup = contactService.getContactCountByGroupId(groupId,start,limit);		
			
			List<Contact> list = contactService.countGroupConatct(groupId);
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("total", list);
			 dataMap.put("groupContactData", listContactByGroup);
			//System.out.println("get Data :-- " + alltemplateList.get(4).getDescription());
			if(listContactByGroup.size() > 0){
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
	@RequestMapping(value = "/getActiveGroupByUserId/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllGroup(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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
			List<GroupDetails> groupActiveDetails = groupDetailsService.getActiveGroupDetailsByUserId(userId);
			
			//System.out.println("get Data :-- " + alltemplateList.get(4).getDescription());
			if((groupActiveDetails.size() > 0) && (!groupActiveDetails.get(0).equals(""))){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "Data found");
				map.put("data", groupActiveDetails);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found, Please Add group..");
				map.put("data", groupActiveDetails);
			}
		}
		return map;
	}
	@RequestMapping(value = "getGroupById/{groupId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getTemplateById(@PathVariable("groupId")int groupId,@RequestHeader("Authorization") String authorization)
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
			List<GroupDetails> groupList = groupDetailsService.getGroupDetailsByGroupId(groupId);
			
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
	@RequestMapping(value = "updateGroupById/{groupId}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>upadteUserById(@PathVariable("groupId")int groupId,@RequestBody String josnString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
	{
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
			mapper = new ObjectMapper();		
			JsonNode node = mapper.readValue(josnString, JsonNode.class);
			
			GroupDetails groupDetails= new GroupDetails();
			groupDetails.setName(node.get("name").asText());
			groupDetails.setStatus(node.get("status").asInt());
			groupDetails.setGroupDescription(node.get("groupDescription").asText());
			
			int result = groupDetailsService.updateGroupDetails(groupDetails,groupId);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 200);
				map.put("message", "updated successfully");
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
	@RequestMapping( value = "/deleteGroup/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deleteTemplate(@PathVariable("groupId") int groupId,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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
			int result = groupDetailsService.deleteGroupDetailsByGroupId(groupId);
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
}
