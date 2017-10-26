package com.mobisoft.sms.restcontroller;

import java.io.File;
import java.io.IOException;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.io.FileUtils;
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
	
	@Value("${downloadUserCsvFile}")
	private String downloadUserCsvFile;
	
	
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
			if(todayMessageCount.size() > 0 || weaklyCountMessage.size() > 0 || monthlyCountMessage.size() > 0){
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
	
	@RequestMapping(value = "dailyRepotMessage/{userId}/{date}/{start}/{max}",method = RequestMethod.GET)
	public Map<String,Object>dailyRepotMessage(@PathVariable("userId")int userId,@PathVariable("date")String date,@PathVariable("start")int start,@PathVariable("max")int max,@RequestHeader("Authorization") String authorization)
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
			
			/*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			String date = dtf.format(localDate);
			System.out.println(dtf.format(localDate));*/
			List<Integer> list = userReportService.messageCountDaily(userId,date);
			System.out.println("count"+list.get(0));
			List<DlrStatus> dalyReport = userReportService.dailyRepotMessage(userId,date,start,max);
			
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
	public Map<String,Object> archiveRepotMessage(@PathVariable("userId")int userId,@PathVariable("startDate")String startDate,
			@PathVariable("endDate")String endDate,@RequestHeader("Authorization") String authorization
			) throws IOException
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
			System.out.println("in list");
			/*
			startDate = startDate.replaceAll("[\\s\\-()]", "");
			endDate = endDate.replaceAll("[\\s\\-()]", "");*/
			List list = userReportService.archiveReportByUserId(userId, startDate, endDate);
			System.out.println("in list");
			if(list.size() > 0)
			{
				map.put("code", 302);
				map.put("status", "success");
				map.put("message", "Create File Successfully");
				map.put("fileName", list.get(0));
			}
		}
		return map;
	}
	@RequestMapping(value = "deleteFile",method = RequestMethod.POST)
	public void  deleteFile() throws IOException
	{
		try{
    		File file = new File(downloadUserCsvFile);    		
    		if(file.isDirectory()){
    			FileUtils.cleanDirectory(file);
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}		
	}
	@RequestMapping(value = "scheduleReportByUserId/{userId}/{fromDate}/{toDate}/{start}/{max}",method = RequestMethod.GET)
	public Map<String,Object>scheduleReportByUserId(@PathVariable("userId")int userId,@PathVariable("fromDate")String fromDate,@PathVariable("toDate")String toDate,@PathVariable("start")int start,@PathVariable("max")int max,@RequestHeader("Authorization") String authorization)
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
			List<UserJobs> dalyReport = userReportService.scheduleReportByUserId(userId,fromDate,toDate, start, max);
			int totalSchedualCount = userReportService.messageCountScheduale(userId,fromDate,toDate);
						
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
	@RequestMapping(value = "compaignReportByUserId/{userId}/{fromDate}/{toDate}/{start}/{max}",method = RequestMethod.GET)
	public Map<String,Object>compaignReportByUserId(@PathVariable("userId")int userId,
			@PathVariable("start")int start,@PathVariable("max")int max,@PathVariable("fromDate")String fromDate,
			@PathVariable("toDate")String toDate,@RequestHeader("Authorization") String authorization)
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
			List<UserJobs> compaignReport = userReportService.compaignStatus(userId, start, max,fromDate,toDate);
			List<UserJobs> totalCompaignCount = userReportService.compaignStatusCount(userId,fromDate,toDate);
			System.out.println(totalCompaignCount.get(0));		
			if(totalCompaignCount.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				if(totalCompaignCount.size() > 0)
				{
					map.put("data", compaignReport);
					map.put("total", totalCompaignCount.get(0));
				}
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", compaignReport);
			}
		}		
		return map;
	}
	@RequestMapping(value = "dlrStatusGroupBy/{userId}/{jobId}",method = RequestMethod.GET)
	public Map<String,Object>dlrStatusGroupBy(@PathVariable("userId")int userId,@PathVariable("jobId")int jobId,@RequestHeader("Authorization") String authorization)
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
			List<DlrStatus> dlrStausReport = userReportService.dlrStatusGroupBy(jobId, userId);
			System.out.println(dlrStausReport.get(0));
			Map<Object,Object> mapDlrStatus = new HashMap<>();
			Iterator itr = dlrStausReport.iterator();
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				System.out.println(object[0]);
				System.out.println(object[1]);
				mapDlrStatus.put(object[0], object[1]);
				
			}
			if(dlrStausReport.size() > 0){
				map.put("status", "success");
				map.put("code", 302);
				map.put("message", "data found");
				map.put("data", mapDlrStatus);
				
			}else{
				map.put("status", "success");
				map.put("code", 204);
				map.put("message", "No data found");
				map.put("data", mapDlrStatus);
			}
		}
		
		return map;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "dlrStausRepotExportDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>dlrStausRepotExportDetails(@RequestBody String jsonString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
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
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			System.out.println("in list");
			System.out.println( node.get("status").asText());
			String status = "";
			status = node.get("status").asText();
			List list = userReportService.dlrStausRepotExportDetails(node.get("userId").asInt(),node.get("jobId").asInt(),status);
			System.out.println("in list"+list.get(0));
			if(list.size() > 0)
			{
				map.put("code", 302);
				map.put("status", "success");
				map.put("message", "Create File Successfully");
				map.put("fileName", list.get(0));
			}
			else if(list.get(0).equals("2"))
			{
				map.put("code", 302);
				map.put("status", "success");
				map.put("message", "No Data Found");
			}
		}
		return map;
		
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "dlrReportDetails",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>dlrReportDetails(@RequestBody String jsonString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
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
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			System.out.println("in list");
			System.out.println( node.get("status").asText());
			String status = "";
			status = node.get("status").asText();
			Map<Integer,List<DlrStatus>> maplist = userReportService.dlrReportDetails(node.get("userId").asInt(),node.get("jobId").asInt(),status,node.get("start").asInt(),node.get("max").asInt());
	
			System.out.println("in list"+maplist.get(0));
			if(maplist.size() > 0)
			{
				//List<DlrStatus> list = maplist.get(1);	
				
				if(maplist.size() > 0)
				{
					map.put("code", 302);
					map.put("status", "success");
					map.put("message", "Show Data Successfully");
					map.put("data", maplist.get(1));
					map.put("total", maplist.get(2).get(0));
				}
				else
				{
					map.put("code", 201);
					map.put("status", "error");
					map.put("message", "No Data Found");
					
				}
				
			}
			else
			{
				
			}
			
		}
		return map;
		
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "searchMobile",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>searchMobile(@RequestBody String jsonString,@RequestHeader("Authorization") String authorization) throws JsonParseException, JsonMappingException, IOException
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
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);		
			List<DlrStatus> serchList = userReportService.searchMobileStatus(node.get("mobile").asText(),node.get("date").asText());
			if(serchList.size() > 0)
			{
				map.put("code", 302);
				map.put("status", "success");
				map.put("data",serchList);
			}
			else			{
				map.put("code", 201);
				map.put("status", "error");
				map.put("message", "No Data Found");
			}
			
		}
		return map;
		
	}
	
}
