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
import com.mobisoft.sms.model.SenderId;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.SenderIdService;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.TokenAuthentication;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class SenderIdRestController {

	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private SenderIdService senderIdService;
	
	private ObjectMapper mapper = null;
	
	@RequestMapping(value = "/saveSenderId",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> saveSenderId(@RequestHeader("Authorization") String authorization,@RequestBody String jsonString) throws JsonParseException, JsonMappingException, IOException{

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
			String sender = node.get("senderId").asText();
			if(sender != null )
			{
				System.out.println("Before Filter"+sender);
				sender =Global.removeAccents(sender);
				System.out.println("After Filter"+sender);				
				if(sender.length() != 6)
				{
					map.put("status", "error");
					map.put("code", 400);
					map.put("message", "Please Enter Valid Lenght Sender Id");
					map.put("data", null);
				}
				else
				{
					User user = new User();
					user.setId(node.get("userId").asInt());
					
					SenderId senderId = new SenderId();
					senderId.setSender_id(sender);
					senderId.setStatus(node.get("status").asInt());
					senderId.setUserId(user);
					int result = senderIdService.saveSenderId(senderId);
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
				
				}
				else
				{
					map.put("status", "error");
					map.put("code", 400);
					map.put("message", "Please Enter Sender Id");
					map.put("data", null);
				}
			
		}
		
		return map;
	}
	
	@RequestMapping(value = "/getAllSenderId",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllSenderId(@RequestHeader("Authorization") String authorization)
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
			List<SenderId> allSenderIdList = senderIdService.getSenderId();
			
			if(allSenderIdList.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "Data found");
				map.put("data", allSenderIdList);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", allSenderIdList);
			}
		}		
		
		return map;
	}
	@RequestMapping(value = "getSenderById/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getSenderById(@PathVariable("id")int id,@RequestHeader("Authorization") String authorization)
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
			List<SenderId> userSenderIdList = senderIdService.getSenderId(id);
			
			if(userSenderIdList.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", userSenderIdList);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", userSenderIdList);
			}
		}
		
		return map;
	}
	@RequestMapping(value = "updateSenderById/{id}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>upadteUserById(@PathVariable("id")int id,@RequestBody String josnString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
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
			
			String sender = node.get("senderId").asText();
			if(sender != null ){
				System.out.println("Before Filter"+sender);
				sender =Global.removeAccents(sender);
				System.out.println("After Filter"+sender);				
				if(sender.length() != 6){
					map.put("status", "error");
					map.put("code", 400);
					map.put("message", "Please Enter Valid Lenght Sender Id");
					map.put("data", null);
				}
				else{
					SenderId senderId = new SenderId();
					senderId.setSender_id(node.get("senderId").asText());
					senderId.setStatus(node.get("status").asInt());
					senderId.setId(id);			
					
					int result = senderIdService.updateSenderId(senderId);
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
			}
			else{
				map.put("status", "error");
				map.put("code", 400);
				map.put("message", "Please Enter Sender Id");
				map.put("data", null);
			}

			
		}
		
		
		return map;
		
	}
	@RequestMapping( value = "/deleteSenderId/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deleteSenderId(@PathVariable("id") int id,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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

			SenderId senderId =new SenderId();
			senderId.setId(id);
					
			int result = senderIdService.deleteSenderId(senderId);
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
