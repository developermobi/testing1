package com.mobisoft.sms.restcontroller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.service.UserReportService;
import com.mobisoft.sms.utility.TokenAuthentication;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class UserReportRestController {

	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private UserReportService userReportService;
	
	private ObjectMapper mapper = null;
	
	@RequestMapping(value = "getTodayMessageCount/{userId}",method = RequestMethod.GET)
	public Map<String,Object>getTodayMessageCount(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
			
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			List<DlrStatus> todayMessageCount = userReportService.todayCountMessage(userId);
			System.out.println(todayMessageCount);
			Iterator itr = todayMessageCount.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[])itr.next();
				map.put(obj[1].toString(), obj[0]);	
			}
			if(todayMessageCount.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
			}
		}
		
		return map;
	}
	@RequestMapping(value = "weeklyCountMessage/{userId}",method = RequestMethod.GET)
	public Map<String,Object>weeklyCountMessage(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
			
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			List<DlrStatus> weeklyCountMessage = userReportService.weeklyCountMessage(userId);
			System.out.println(weeklyCountMessage);
			Iterator itr = weeklyCountMessage.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[])itr.next();
				map.put(obj[1].toString(), obj[0]);	
			}
			if(weeklyCountMessage.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
			}
		}
		
		return map;
	}
	@RequestMapping(value = "monthlyCountMessage/{userId}",method = RequestMethod.GET)
	public Map<String,Object>monthlyCountMessage(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
			
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			List<DlrStatus> monthlyCountMessage = userReportService.monthlyCountMessage(userId);
			System.out.println(monthlyCountMessage);
			Iterator itr = monthlyCountMessage.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[])itr.next();
				map.put(obj[1].toString(), obj[0]);	
			}
			if(monthlyCountMessage.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
			}
		}
		
		return map;
	}
	@RequestMapping(value = "dailyRepotMessage/{userId}/{start}/{max}",method = RequestMethod.GET)
	public Map<String,Object>dailyRepotMessage(@PathVariable("userId")int userId,@PathVariable("start")int start,@PathVariable("max")int max,@RequestHeader("Authorization") String authorization)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
			
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			List<DlrStatus> dalyReport = userReportService.dailyRepotMessage(userId,start,max);
						
			if(dalyReport.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", dalyReport);
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", dalyReport);
			}
		}
		
		return map;
	}
	@RequestMapping(value = "archiveRepotMessage/{userId}",method = RequestMethod.GET)
	public Map<String,Object>dailyRepotMessage(@PathVariable("userId")int userId,@RequestHeader("Authorization") String authorization)
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
			
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			List<DlrStatus> dalyReport = userReportService.archiveReportByUserId(userId);
						
			if(dalyReport.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", dalyReport);
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", dalyReport);
			}
		}
		
		return map;
	}
}
