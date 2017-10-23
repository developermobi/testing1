package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.TokenAuthentication;
import com.mobisoft.sms.model.Credit;
import com.mobisoft.sms.model.Debit;
import com.mobisoft.sms.model.SmsBalance;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.service.UserService;

@CrossOrigin
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
	
	@Value("${sms_username}")
	private String userNameMessage;
	

	@Value("${password}")
	private String password;
	

	@Value("${senderId}")
	private String senderId;
	
	
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
				Map<String, Object> mapData =new HashMap<>();
				mapData.put("authorization","Basic " + token);
				mapData.put("fullName", userList.get(0).getName());
				mapData.put("userId", userList.get(0).getUserId());
				mapData.put("userRole", userList.get(0).getRole());
 				map.put("status", 302);
				map.put("message", "success");
				map.put("data", mapData);

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
		else if(tokenAuthentication.validateToken(authorization) == 1 || tokenAuthentication.validateToken(authorization) == 2){

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
	
	@RequestMapping(value = "getUserByResellerId/{userId}/{start}/{max}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getUserByResellerId(@PathVariable("userId")int userId,@PathVariable("start")int start,@PathVariable("max")int max,@RequestHeader("Authorization") String authorization)
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
		else if(tokenAuthentication.validateToken(authorization) == 1 ){

			Map<Integer,List<User>> userList = userService.getUserByResellerId(userId,start,max);
			if((userList.size() > 0) && (!userList.get(0).get(0).equals(""))){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", userList.get(0));
				map.put("total", userList.get(1));
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
		mapper = new ObjectMapper();		
		JsonNode node = mapper.readValue(josnString, JsonNode.class);
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}
		else if(tokenAuthentication.validateToken(authorization) == 1 || tokenAuthentication.validateToken(authorization) == 2){
			User user = new User();
			
			user.setName(node.get("name").asText());
			user.setEmail(node.get("email").asText());
			user.setMobile(node.get("mobile").asText());
			user.setCity(node.get("city").asText());
			user.setState(node.get("state").asText());
			user.setCountry(node.get("country").asText());
			user.setAddress(node.get("address").asText());
			user.setCompanyName(node.get("companyName").asText());
			user.setUserId(userId);
			user.setRole(node.get("role").asInt());
			
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
		else{
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}		
		
		return map;
		
	}
	
	@RequestMapping( value = "/deleteUser/{userId}/{resellerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deleteAdmin(@PathVariable("userId") int userId,@PathVariable("resellerId") int resellerId,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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
					
			int result = userService.deleteUser(userId,resellerId);
			if(result == 1){
				map.put("status", "success");
				map.put("code", 200);
				map.put("message", "deleted successfully");
				map.put("data", result);
			}
			else if(result ==2){
			
				map.put("status", "error");
				map.put("code", 404);
				map.put("message", "Not found User");
				map.put("data", result);
			}
			else{
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
	
	@RequestMapping(value = "getBalanceByUserId/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getBalanceByUserId(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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

			List<SmsBalance> userList = userService.getBalanceByUserId(userId);
			System.out.println(userList.size());
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
		
		return map;
	}
	
	@RequestMapping(value = "getCreditByUserId/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getCreditByUserId(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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

			List<Credit> userList = userService.getCreditDetailsByUserId(userId);
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
		
		return map;
	}
	
	@RequestMapping(value = "getDebitByUserId/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getDebitByUserId(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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

			List<Debit> userList = userService.getDebitByUserId(userId);
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
		
		return map;
	}
	@RequestMapping( value = "/addCreditByReseller/{userId}/{resellerId}/{productId}/{newBalance}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> addCreditByReseller(@PathVariable("userId") int userId,@PathVariable("resellerId") int resellerId,@PathVariable("productId") int productId,@PathVariable("newBalance") int newBalance,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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
					
			//int result = userService.addCreditUser(userId, resellerId, productId, newBalance);
			System.out.println("Sms Balnce");
			List<Integer> balance = smsHelperService.getBalance(resellerId,productId);
			
			
			if(balance.size() > 0)
			{
				int resellerBalnce = balance.get(0);

				if(newBalance > 0)
				{
					int updateResellerBalance = resellerBalnce - newBalance;
					if(updateResellerBalance > 0)
					{
						
						int result = userService.addCreditUser(userId, resellerId, productId, newBalance);
						if(result == 1){
							map.put("status", "success");
							map.put("code", 201);
							map.put("message", "Add Credit successfully");
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
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		return map;	
	}
	
	@RequestMapping(value = "addProduct/{userId}/{reselerId}/{prodcutId}/{balance}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>addproduct(@PathVariable("userId")int userId,@PathVariable("reselerId")int reselerId,@PathVariable("prodcutId")int prodcutId,@PathVariable("balance")int balance,@RequestHeader("Authorization") String authorization)
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
			List<SmsBalance> list = smsHelperService.findProdcut(userId, prodcutId);
			System.out.println(list.size());
			if(list.isEmpty())
			{
				if(balance > 0)
				{
					int result = userService.addProdcut(reselerId, userId, prodcutId, balance);
					if(result == 1){
						map.put("status", "success");
						map.put("code", 201);
						map.put("message", "Add Prodcut Successfully");
						map.put("data", result);
					}else if(result == 3){
						map.put("status", "error");
						map.put("code", 204);
						map.put("message", "Insufficieant Balance");
						map.put("data", result);
					}
					else if(result == 2){
						map.put("status", "error");
						map.put("code", 204);
						map.put("message", "Please Purchase Prodcut");
						map.put("data", result);
					}
					else{
						map.put("status", "error");
						map.put("code", 400);
						map.put("message", "error occured during add prodcut");
						map.put("data", result);
					}
					
				}
				else
				{
					map.put("code", 405);
					map.put("status", "error");
					map.put("message", "Please set positive balance");
				}
			}else{
				map.put("code", 409);
				map.put("status", "error");
				map.put("message", "All ready exists");
			}
			
			
		}
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		return map;	

	}
	@RequestMapping( value = "/deductCreditByReseller/{userId}/{resellerId}/{productId}/{newBalance}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deductCreditByReseller(@PathVariable("userId") int userId,@PathVariable("resellerId") int resellerId,@PathVariable("productId") int productId,@PathVariable("newBalance") int newBalance,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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
					
			//int result = userService.addCreditUser(userId, resellerId, productId, newBalance);
			System.out.println("Sms Balnce");
			List<Integer> balance = smsHelperService.getBalance(userId,productId);
			
			
			if(balance.size() > 0)
			{
				int userBalnce = balance.get(0);

				if(newBalance > 0)
				{
					int updateResellerBalance = userBalnce - newBalance;
					if(updateResellerBalance > 0)
					{
						
						int result = userService.addCreditUser(resellerId ,userId , productId, newBalance);
						if(result == 1){
							map.put("status", "success");
							map.put("code", 201);
							map.put("message", "Dedcut Balance successfully");
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
		else if(tokenAuthentication.validateToken(authorization) == 2){
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");
		}
		return map;	
	}
	@RequestMapping(value = "validateUserName/{userName}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>validateUserName(@PathVariable("userName")String userName)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		List<User>listUser= userService.validateUserName(userName);
		
		if(listUser.size() > 0){
			
			if(listUser.get(0).getUserName().equals(userName))
			{
				// Send Mail And message to user with password
				
				String message ="Dear Sir, Your password has been send on your register mobile number"+listUser.get(0).getPassword();
				String mobile = listUser.get(0).getMobile();
				if( mobile.length() == 12)
				{
					mobile = mobile.substring(2);
				}
				try {
					int i = Global.sendMessage(userNameMessage, password,mobile, senderId, message);
					if(i == 1)
					{
						map.put("code", 201);
						map.put("status", "success");
						map.put("message", "Dear Sir, Your password has been send on your register mobile number");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*String otp = smsHelperService.genrateOtp(listUser.get(0).getUserId());
				System.out.println("New Otp"+otp);*/
			}
			else
			{
				map.put("code", 404);
				map.put("status", "error");
				map.put("message", "Invalid User Name");
			}
		}
		else {
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Not Found User name");
		}		
		return map;
	}
	@RequestMapping(value = "generateOtp/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>generateOtp(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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
		else
		{
			int temp = smsHelperService.genrateOtp(userId);			
			if(temp == 1){
				// send mail and message
				map.put("code", 201);
				map.put("status", "success");
				map.put("message", "Otp send on your register mobile number");
			}
			else {
				map.put("code", 404);
				map.put("status", "error");
				map.put("message", "Not Found User name");
			}		
		}		
		return map;
	}
	@RequestMapping(value = "updatePassword",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>updatePassword(@RequestBody String jsonString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		if(tokenAuthentication.validateToken(authorization) == 0)
		{
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else
		{
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);			
			int chnagePassword = userService.changePassword(node.get("oldPassword").asText(), node.get("newPassword").asText(), node.get("userId").asInt());			
			System.out.println("chnpassword ----- "+chnagePassword);
			if(chnagePassword == 1){				
				map.put("code", 201);
				map.put("status", "success");
				map.put("message", "Dear User, Your new password has been send on your register mobile number");
			}
			else if(chnagePassword == 2){
				
				map.put("code", 406);
				map.put("status", "error");
				map.put("message", "Old Password did not match");
			}	
			else
			{
				map.put("code", 404);
				map.put("status", "error");
				map.put("message", "Somthig Going Worg...");
			}
		}
		return map;
	}
	@RequestMapping(value = "verifyOtp/{otp}/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>verifyOtp(@PathVariable("userId")int userId,@PathVariable("otp")String otp,@RequestHeader("Authorization") String authorization)
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
		else
		{
			int temp = smsHelperService.varifyOtp(otp, userId);	
			if(temp == 1){
				// send mail and message
				map.put("code", 201);
				map.put("status", "success");
				map.put("message", "Otp Match Successfully");
			}
			else {
				map.put("code", 404);
				map.put("status", "error");
				map.put("message", "otp not match");
			}
		}		
		return map;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getTransactionDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> getTransactionDetails(@RequestHeader("Authorization") String authorization,@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{
		
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
			JsonNode node = mapper.readValue(jsonString,JsonNode.class);
			if(node.get("userId").asInt() != 0 &&  node.get("type").asInt() != 0 && node.get("productId").asInt() != 0)
			{
				Map<Integer,List> mapList = userService.countTransactionList(node.get("userId").asInt(), node.get("type").asInt(), node.get("productId").asInt());
				System.out.println("map "+mapList.get(0));
				
				List list = new ArrayList<>();
				list.add(mapList.get(0).get(0));
				System.out.println("list"+list.get(0));
				
				if(!list.isEmpty() && (!list.get(0).equals("noUserId")) && (!list.get(0).equals("noProductId")))
				{
					List transactionList = userService.transactionList(node.get("userId").asInt(),
							node.get("type").asInt(), node.get("productId").asInt(), node.get("start").asInt(), 
							node.get("limit").asInt());					
					if(transactionList.size() > 0)
					{
						map.put("status", "error");
						map.put("code", 204);
						map.put("message", "No data found");
						map.put("data", transactionList);
						map.put("total", list.get(0));
					}
					else
					{
						map.put("status", "error");
						map.put("code", 204);
						map.put("message", "No data found");
						map.put("data", null);
					}
				}
				else
				{
					map.put("status", "error");
					map.put("code", 404);
					map.put("message", "Some parameter is not match in our  Db");
					map.put("data", null);
				}
				
				
				
			}
			else
			{
				map.put("status", "error");
				map.put("code", 422);
				map.put("message", "Invalid Parameter ");
				map.put("data", null);
			}
			
		}		
		return map;
		
	}

}
