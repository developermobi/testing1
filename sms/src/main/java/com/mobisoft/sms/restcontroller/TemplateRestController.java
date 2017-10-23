package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.mobisoft.sms.model.Template;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.TemplateService;
import com.mobisoft.sms.utility.TokenAuthentication;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class TemplateRestController {

	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private TemplateService templateService;
	
	private ObjectMapper mapper = null;
	
	@RequestMapping(value = "/saveTemplate",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
		else
		{
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			if(node.get("name").asText().length() != 0 && node.get("description").asText().length() != 0)
			{
				User user = new User();
				user.setUserId(node.get("userId").asInt());
				
				Template template = new Template();
				template.setDescription(node.get("description").asText());
				template.setName(node.get("name").asText());
				template.setUserId(user);
				int result = templateService.saveTemplate(template);
				if(result == 1){
					map.put("status", "success");
					map.put("code", 201);
					map.put("message", "Template added successfully");
					map.put("data", result);
				}else{
					map.put("status", "error");
					map.put("code", 400);
					map.put("message", "Somthig Going Worng...");
					map.put("data", result);
				}
			}
			else
			{
				map.put("status", "error");
				map.put("code", 406);
				map.put("message", "Please Enter Title and Description Valid Data");
				map.put("data", null);
			}
			
			
		}
		
		return map;
	}
	
	@RequestMapping(value = "/getAllTemplate/{userId}/{start}/{limit}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllTemplate(@PathVariable("userId")int userId,@PathVariable("start")int start,@PathVariable("limit")int limit,@RequestHeader("Authorization") String authorization)
	{
		System.out.println(userId);
		System.out.println("Start: "+start);
		System.out.println("limit: "+limit);
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
			List<Template> alltemplateListCount = templateService.getTemplateByUserId(userId);
			List<Template> alltemplateList = templateService.getTemplateByUserIdPaginate(userId, start, limit);
			
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("total", alltemplateListCount.size());
			 dataMap.put("template_data", alltemplateList);
			//System.out.println("get Data :-- " + alltemplateList.get(4).getDescription());
			if(alltemplateListCount.size() > 0){
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
	@RequestMapping(value = "getTemplateById/{templateId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getTemplateById(@PathVariable("templateId")int templateId,@RequestHeader("Authorization") String authorization)
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
			List<Template> userTemplateList = templateService.getTemplateById(templateId);
			System.out.println("Template List  "+userTemplateList.size());
			if(userTemplateList.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", userTemplateList);
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", userTemplateList);
			}
		}
		
		return map;
	}
	@RequestMapping(value = "updateTemplateById/{templateId}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>upadteUserById(@PathVariable("templateId")int templateId,@RequestBody String josnString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
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
			
			Template template = new Template();
			template.setDescription(node.get("description").asText());
			template.setName(node.get("name").asText());
			template.setId(templateId);
			
			int result = templateService.updateTemplate(template);
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
	@RequestMapping( value = "/deleteTemplate/{templateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> deleteTemplate(@PathVariable("templateId") int templateId,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException{
		
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

			Template template =new Template();
			template.setId(templateId);
					
			int result = templateService.deleteTemplate(template);
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
	@RequestMapping(value = "/getAllTemplateCountByUser/{userId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>getAllTemplate(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
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
			List<Template> alltemplateListCount = templateService.getTemplateByUserId(userId);
		
			
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("total", alltemplateListCount.size());
			 
			//System.out.println("get Data :-- " + alltemplateList.get(4).getDescription());
			if(alltemplateListCount.size() > 0){
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
