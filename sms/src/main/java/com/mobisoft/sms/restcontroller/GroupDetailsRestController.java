package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.GroupDetails;
import com.mobisoft.sms.model.SenderId;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.GroupDetailsService;
import com.mobisoft.sms.service.UserService;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.TokenAuthentication;

@RestController
@RequestMapping(value = "/api")
public class GroupDetailsRestController {

	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private GroupDetailsService groupDetailsService;
	
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
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else
		{
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			User user = new User();
			user.setId(node.get("userId").asInt());
			
			GroupDetails groupDetails = new GroupDetails();
			groupDetails.setName(node.get("name").asText());
			groupDetails.setStatus(node.get("status").asInt());
			
			Set<GroupDetails> groupDetailsId = new HashSet<GroupDetails>();
			groupDetailsId.add(groupDetails);
		//	user.setGroupDetails(groupDetailsId);
			
			int result = userService.saveUser(user);
				if(result == 1){
					map.put("status", "success");
					map.put("code", 201);
					map.put("message", "inserted successfully");
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
}
