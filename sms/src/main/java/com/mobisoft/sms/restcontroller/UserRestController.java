package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserRestController {
	
	@Autowired
	UserService userService;
	
	private ObjectMapper mapper = null;
	
	@RequestMapping(value = "/saveUser",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> saveUser(@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{

		Map<String,Object> map = new HashMap<>();		
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		String password = Global.randomString(6);
		
		mapper = new ObjectMapper();
		JsonNode node = mapper.readValue(jsonString, JsonNode.class);
		User user = new User();
		user.setUserName(node.get("userName").asText());
		user.setPassword(password);
		user.setName(node.get("name").asText());
		user.setEmail(node.get("email").asText());
		user.setMobile(node.get("mobile").asText());
		user.setCity(node.get("city").asText());
		user.setState(node.get("state").asText());
		user.setCountry(node.get("country").asText());
		user.setAddress(node.get("address").asText());
		user.setRole(node.get("role").asInt());
		user.setStatus(node.get("status").asInt());
		user.setCompany_name(node.get("companyName").asText());
		
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
			

				
		return map;
	}
	

}
