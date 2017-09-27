package com.mobisoft.sms.restcontroller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.SmsDnd;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.DlrStatusService;
import com.mobisoft.sms.service.SmsHelperService;


@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class DlrStatusRestController {
	
	@Autowired
	DlrStatusService dlrStatusService;
	
	@Autowired
	SmsHelperService smshelperService;
	
	private ObjectMapper mapper;
	
	@Value("${schedualTimerIn}")
	private String  schedualTimerIn;
	
	//Send sms from user_jobs table
	
	@Scheduled(fixedRateString  ="${schedualTimerIn}")
	@RequestMapping(value = "/saveDlrStatus",method = RequestMethod.POST)
	public void saveUser() throws FileNotFoundException, IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 404);
		map.put("message", "Data Not Inserted");
		
		System.out.println("Schedular Method Start");
		//DlrStatus dlrStatus = new DlrStatus();
		int result = dlrStatusService.saveDlrStatus();
		System.out.println(result);
		if(result == 1)
		{
			map.put("status", 201);
			map.put("message", "Sussessfully Insert batch");
			System.out.println("Successfully save data");
		}
		else
		{
			System.out.println("Successfully not save data");
		}
		
	}
	
	
	/*@Scheduled(fixedDelay=5000)
	@RequestMapping(value = "/saveDlrStatus",method = RequestMethod.POST)
	public Map<String,Object> saveUser() throws FileNotFoundException, IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 404);
		map.put("message", "Data Not Inserted");
		
		System.out.println("askldjalskdjlasdjlasdjasjd akjsdhklasd adbasd asdas");
		//DlrStatus dlrStatus = new DlrStatus();
		int result = dlrStatusService.saveDlrStatus();
		if(result == 1)
		{
			map.put("status", 201);
			map.put("message", "Sussessfully Insert batch");
		}
		return map;
	}
*/
/*	@RequestMapping(value = "/getNumberList",method = RequestMethod.GET)
	public Map<String,Object> getNumberList() throws FileNotFoundException, IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 404);
		map.put("message", "Data Not Inserted");
		
		System.out.println("askldjalskdjlasdjlasdjasjd akjsdhklasd adbasd asdas");
		DlrStatus dlrStatus = new DlrStatus();
		List<SmsDnd> list = smshelperService.filterDndNumber();
		System.out.println(list.size());
		if(list.size() > 0)
		{	
		}
		return map;
	}*/
}
