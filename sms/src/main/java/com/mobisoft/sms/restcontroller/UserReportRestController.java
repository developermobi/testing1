package com.mobisoft.sms.restcontroller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.UserJobs;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getDashboardCount/{userId}",method = RequestMethod.GET)
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
			Map< String,Object> mapListToday = new HashMap<>();
			while (itr.hasNext()) {
				Object[] obj = (Object[])itr.next();
				mapListToday.put(obj[1].toString(), obj[0]);	
			}
			Map< String,Object> mapListWeakly = new HashMap<>();
			List<DlrStatus> weaklyCountMessage = userReportService.weeklyCountMessage(userId);
			System.out.println(weaklyCountMessage);
			Iterator itrWeakly = weaklyCountMessage.iterator();
			while (itrWeakly.hasNext()) {
				Object[] obj = (Object[])itrWeakly.next();
				mapListWeakly.put(obj[1].toString(), obj[0]);	
			}
			Map< String,Object> mapListMonthly = new HashMap<>();
			List<DlrStatus> monthlyCountMessage = userReportService.monthlyCountMessage(userId);
			System.out.println(monthlyCountMessage);
			Iterator monthlyTtr = monthlyCountMessage.iterator();
			while (monthlyTtr.hasNext()) {
				Object[] obj = (Object[])monthlyTtr.next();
				mapListMonthly.put(obj[1].toString(), obj[0]);	
			}
			if(todayMessageCount.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("todayCount",mapListToday);
				map.put("weaklyCount",mapListWeakly);
				map.put("monthlyCount",mapListMonthly);
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
			}
		}
		
		return map;
	}
	/*@RequestMapping(value = "weeklyCountMessage/{userId}",method = RequestMethod.GET)
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
	}*/
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
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			String date = dtf.format(localDate);
			System.out.println(dtf.format(localDate));
			List<Integer> list = userReportService.messageCountDaily(userId,date);
			System.out.println("count"+list.get(0));
			List<DlrStatus> dalyReport = userReportService.dailyRepotMessage(userId,start,max);
			
			if(dalyReport.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", dalyReport);
				if(list.size() > 0)
				{
					map.put("total", list.get(0));
				}
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", dalyReport);
			}
		}
		
		return map;
	}

	@RequestMapping(value = "archiveRepotMessage/{userId}/{startDate}/{endDate}",method = RequestMethod.GET)
	public HttpServletResponse archiveRepotMessage(@PathVariable("userId")int userId,@PathVariable("startDate")String startDate,@PathVariable("endDate")String endDate,@RequestHeader("Authorization") String authorization) throws IOException
	{
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		HttpServletResponse response = null;
		if(tokenAuthentication.validateToken(authorization) == 0){
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
		}
		else{
			System.out.println("in list");
			List list = userReportService.archiveReportByUserId(userId, startDate, endDate);
			System.out.println("in list");
		}
		return response;
	}
	@RequestMapping(value = "scheduleReportByUserId/{userId}/{start}/{max}",method = RequestMethod.GET)
	public Map<String,Object>scheduleReportByUserId(@PathVariable("userId")int userId,@PathVariable("start")int start,@PathVariable("max")int max,@RequestHeader("Authorization") String authorization)
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
			List<UserJobs> dalyReport = userReportService.scheduleReportByUserId(userId, start, max);
			int totalSchedualCount = userReportService.messageCountScheduale(userId);
						
			if(dalyReport.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", dalyReport);
				map.put("total", totalSchedualCount);
				
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
