package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.TokenAuthentication;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.service.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserRestController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private SmsHelperService smsHelperService;
	
	private ObjectMapper mapper = null;
	
	
	@RequestMapping(value="/user/login",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> userAuthentication(@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 404);
		map.put("message", "user not found");
		
		mapper = new ObjectMapper();
		User user = mapper.readValue(jsonString, User.class);
		
		List<User> userList = userService.getUserByUserName(user.getUserName());
		if(userList.size() > 0){
			
			if(userList.get(0).getUserName().equals(user.getUserName()) && userList.get(0).getPassword().equals(user.getPassword())){
				String token = tokenAuthentication.getToken(user.getUserName(),user.getPassword());
				
				map.put("status", 302);
				map.put("message", "success");
				map.put("authorization", "Basic " + token);
				map.put("fullName", userList.get(0).getName());
				map.put("userId", userList.get(0).getId());
				
			}
			
		}
		
		return map;
		
	}
	
	
	
	@RequestMapping(value = "/saveUser",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> saveUser(@RequestHeader("Authorization") String authorization,@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{

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
		else if(tokenAuthentication.validateToken(authorization) == 1){
			String password = Global.randomString(6);

			
			
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			List<User> listUser = userService.getUserByUserName(node.get("userName").asText());
			
			
			
			if(listUser.size() > 0)
			{
				map.put("code", 409);
				map.put("status", "error");
				map.put("message", "All Ready Exists User Name "+node.get("userName").asText());
			}
			else
			{
				System.out.println("Sms Balnce");
				List<Integer> balance = smsHelperService.getBalance(node.get("userId").asInt(),node.get("productId").asInt());
				
				
				if(balance.size() > 0)
				{
					int resellerBalnce = balance.get(0);
					//int resellerBalnce = (Integer)smsBalance.getBalance();
					System.out.println("Sms Balnce"+resellerBalnce);
					/*User user = new User();
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
					user.setCompany_name(node.get("companyName").asText());*/
					
					int newUserBalnce = node.get("creditBalnce").asInt();
					if(newUserBalnce > 0)
					{
						int updateResellerBalance = resellerBalnce - newUserBalnce;
						if(updateResellerBalance > 0)
						{
							ObjectNode objectNode =(ObjectNode)node;
							objectNode.put("balance", newUserBalnce);
							objectNode.put("updateResellerBalance",updateResellerBalance);
							objectNode.put("previousResellerBalance",resellerBalnce);
							int result = userService.saveUerDeatils(objectNode);
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
						else
						{
							map.put("code", 204);
							map.put("status", "error");
							map.put("message", "Insufficieant Balance");
						}
						
					}
					else
					{
						map.put("code", 405);
						map.put("status", "error");
						map.put("message", "Please set positive balance ");
					}
					
						
				}
				else
				{
					map.put("code", 406);
					map.put("status", "error");
					map.put("message", "Please Purchase Selected  Product");
				}
				
			}
			
		}else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		

		return map;
	}
	
	@RequestMapping(value = "/getAllUser",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllUser(@RequestHeader("Authorization") String authorization)
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
		else if(tokenAuthentication.validateToken(authorization) == 1){
			List<User> allUserList = userService.getUser();
			if(allUserList.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "Data found");
				map.put("data", allUserList);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", allUserList);
			}
		}
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		
		
		return map;
	}
	
	@RequestMapping(value = "getUserById/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getUserById(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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
		else if(tokenAuthentication.validateToken(authorization) == 1){

			List<User> userList = userService.getUserById(userId);
			if(userList.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", userList);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", userList);
			}
		}
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		
		return map;
	}
	@RequestMapping(value = "updateUserById/{userId}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>upadteUserById(@PathVariable("userId")int userId,@RequestBody String josnString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
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
		else if(tokenAuthentication.validateToken(authorization) == 1){

			mapper = new ObjectMapper();		
			JsonNode node = mapper.readValue(josnString, JsonNode.class);
			
			User user = new User();
			user.setUserName(node.get("userName").asText());
			user.setName(node.get("name").asText());
			user.setEmail(node.get("email").asText());
			user.setMobile(node.get("mobile").asText());
			user.setCity(node.get("city").asText());
			user.setState(node.get("state").asText());
			user.setCountry(node.get("country").asText());
			user.setAddress(node.get("address").asText());
			user.setRole(node.get("role").asInt());
			user.setStatus(node.get("status").asInt());
			user.setCompanyName(node.get("companyName").asText());
			user.setId(userId);
			
			int result = userService.updateUser(user);
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
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		
		
		return map;
		
	}
	
	@RequestMapping( value = "/deleteUser/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deleteAdmin(@PathVariable("userId") int userId,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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
		else if(tokenAuthentication.validateToken(authorization) == 1){

			User user = new User();
			user.setStatus(2);
			user.setId(userId);
					
			int result = userService.deleteUser(user);
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
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		return map;	
	}
	

}
