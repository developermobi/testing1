package com.mobisoft.sms.restcontroller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.model.UserProduct;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.service.UserJobsService;
import com.mobisoft.sms.utility.EmailAPI;
import com.mobisoft.sms.utility.Global;
import com.mobisoft.sms.utility.TokenAuthentication;


import au.com.bytecode.opencsv.CSVReader;

import org.springframework.http.MediaType;


@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class UserJobsResController {
	
	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private UserJobsService userJobsService;
	
	@Autowired
	private SmsHelperService smsHelperService;
	
	private ObjectMapper mapper = null;
	
	@Value("${uploadUserTextFile}")
	private String uploadUserJobsFile;
	
	@Value("${uploadUserGroupData}")
	private String uploadUserGroupData;

	/*@Value("${uploadLiveUrl}")
	private String uploadLiveUrl;*/
	
	private BufferedReader br = null;
	
	private FileReader fr = null;
	
	private File userJobFile;
	
	private String fileName;
	
	private List<String> mobileList;
	
	String rootPath = System.getProperty("catalina.home");
	
	//................................Send Notepad and csv  Message Url..................................................
	
	@RequestMapping(value="/saveUserJobs",method = RequestMethod.POST)
	public Map<String,Object>saveUserJobs(@RequestHeader("Authorization") String authorization,@RequestParam("file")MultipartFile multipartFile,
			@RequestParam("userId")int userId,@RequestParam("message")String message,
			@RequestParam("messageType")int messageType,@RequestParam("sender")String sender,
			@RequestParam("productId")int productId,
			@RequestParam("scheduledAt")String scheduledAt,			
			@RequestParam("jobType")int jobType,@RequestParam("duplicateStatus")int duplicateStatus,@RequestParam("scheduleStatus")int scheduleStatus
			) throws IllegalStateException, ParseException, IOException{

		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1 || tokenAuthentication.validateToken(authorization) == 2 ){
			
			String fileName = "";
			if(!multipartFile.isEmpty()){
				
			    try {
			    	List<Object> dndNumberList=null;
			    	List<UserAuthrization> listCheckAutherization = null;
			    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			        long time = cal.getTimeInMillis();
			    	fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			    	String newFileName = userId+time+fileName;
			    	System.out.println(multipartFile.getSize());
			    	if(multipartFile.getSize() <= 3000000 )
			    	{
			    		if(multipartFile.getOriginalFilename().endsWith(".txt")){
				    		
							String fileUploadDirectory =  uploadUserJobsFile+"/";						
							userJobFile = new File(fileUploadDirectory);						
					        if (!userJobFile.exists()) {
					            if (!userJobFile.mkdirs()) {
					            	
					            	map.put("code", 403);
					    			map.put("status", "error");
					    			map.put("message", "file Upload Directory has not found");
					            }
					        }
							userJobFile = new File(fileUploadDirectory,newFileName);
							multipartFile.transferTo(userJobFile);
							fr = new FileReader(userJobFile);
							br = new BufferedReader(fr);
							String line;
							mobileList = new ArrayList<>();
							while ((line = br.readLine()) != null) {
								line=line.trim();
								if(!line.equals(""))
								{
									mobileList.add(line);
								}
							}
							br.close();
							fr.close();
							listCheckAutherization = smsHelperService.getUserAuthrizationCheck(userId,productId);
							if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
							{	 
								String mobileNumber = String.join(",",mobileList);
								dndNumberList = smsHelperService.mobileNumber(mobileNumber);			   
								List<String> sendMobileLis =new ArrayList<>();
								for(Object b:dndNumberList)
								{
									sendMobileLis.add(String.valueOf(b));
								}
								mobileNumber = String.join(",",sendMobileLis.get(0));
								mobileNumber = mobileNumber.replaceAll("[\\[\\](){}]","");
								FileWriter fw = new FileWriter(userJobFile.getAbsoluteFile());
								BufferedWriter bw = new BufferedWriter(fw);
								mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
								for(String contact : mobileList)
								{
									bw.write(contact);
								    bw.newLine();
								}							        
								bw.close();
							}
							if(duplicateStatus == 1)
							{	 
							  	mobileList =  Global.filterDuplicateNumber(mobileList);							  
							  	FileWriter fw = new FileWriter(userJobFile.getAbsoluteFile());
						        BufferedWriter bw = new BufferedWriter(fw);						       
						        for(String contact : mobileList)
						        {
						        	bw.write(contact);
							        bw.newLine();
						        }						        
						        bw.close();
							}							
						}
			    		else if(multipartFile.getOriginalFilename().endsWith(".csv")){
			    			System.out.println("Upload Csv File Details");
			    			String fileUploadDirectory =  uploadUserJobsFile+"/";						
							userJobFile = new File(fileUploadDirectory);						
					        if (!userJobFile.exists()) {
					            if (!userJobFile.mkdirs()) {
					            	
					            	map.put("code", 403);
					    			map.put("status", "error");
					    			map.put("message", "file Upload Directory has not found");
					            }
					        }
							userJobFile = new File(fileUploadDirectory,newFileName);
							multipartFile.transferTo(userJobFile);
							CSVReader reader = new CSVReader(new FileReader(userJobFile));
							String [] nextLine;
							mobileList = new ArrayList<>();
							 while ((nextLine = reader.readNext()) != null) {
								
								 String number = nextLine[0];
								 mobileList.add(number);
					       } 		           
				           reader.close();
				           listCheckAutherization = smsHelperService.getUserAuthrizationCheck(userId,productId);
				           System.out.println(listCheckAutherization.get(0).getDndCheck());
							
							if(listCheckAutherization.get(0).getDndCheck().equals("Y")){	 
								String mobileNumber = String.join(",",mobileList);
								dndNumberList = smsHelperService.mobileNumber(mobileNumber);
								List<String> sendMobileLis =new ArrayList<>();
								for(Object b:dndNumberList)
								{
									sendMobileLis.add(String.valueOf(b));
								}
								mobileNumber = String.join(",",sendMobileLis.get(0));
								mobileNumber = mobileNumber.replaceAll("[\\[\\](){}]","");
								FileWriter fw = new FileWriter(userJobFile.getAbsoluteFile());
								BufferedWriter bw = new BufferedWriter(fw);
								mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
								for(String contact : mobileList)
								{
								   bw.write(contact);
								   bw.newLine();
								}
								bw.close();
							}
							if(duplicateStatus == 1)
							{	 
								mobileList =  Global.filterDuplicateNumber(mobileList);								  
								FileWriter fw = new FileWriter(userJobFile.getAbsoluteFile());
								BufferedWriter bw = new BufferedWriter(fw);
						        for(String contact : mobileList)
						        {
						        	bw.write(contact);
							        bw.newLine();
						        }
						        bw.close();
						        System.out.println("fileter mobile number"+mobileList);
							}				           
			    		}
			    		else
			    		{
			    			map.put("code", 415);
			    			map.put("status", "error");
			    			map.put("message", "Unsupported File Format");
			    		}
			    		
			    		int messageLength = message.length();
			    		int messageCount = smsHelperService.messageCount(messageType, messageLength);
			    		if(messageCount > 10)
			    		{
			    			map.put("code", 413);
							map.put("status", "error");
							map.put("message", "Message Count Too Large");
			    		}
			    		else
			    		{
			    			if(multipartFile.getOriginalFilename().endsWith(".txt") || multipartFile.getOriginalFilename().endsWith(".csv"))
			    			{
			    				if((mobileList.size() > 0) && !("".equals(mobileList.get(0))))
				    			{
				    				List<Integer> balance = smsHelperService.getBalance(userId,productId);
					    			int sentMessage = mobileList.size() * messageCount;
					    			if(sentMessage <= balance.get(0))
					    			{
					    				List<UserProduct>routeList= smsHelperService.getRouteDetails(userId, productId);
					    				if(routeList.size() > 0)
					    				{
						    				int updateNewBalance = balance.get(0)-sentMessage; 
						    				UserJobs userJobs= new UserJobs();
											userJobs.setUserId(userId);
//											System.out.println("message type :-- "+messageType);
//											if(messageType == 3)
//											{
//												//byte [] b  = message.getBytes("UTF-8");	
//												message = URLEncoder.encode(message, "UTF-8");
//												userJobs.setMessage(message);
//												
//												
//											}
//											else 
//											{
//												userJobs.setMessage(message);
//											}
											userJobs.setMessage(message);
											userJobs.setMessageType(messageType);
											userJobs.setMessageLength(messageLength);
											userJobs.setCount(messageCount);
											userJobs.setSender(sender);
											userJobs.setTotalNumbers(mobileList.size());
											userJobs.setTotalSent(sentMessage);
											userJobs.setFilename(userJobFile.getAbsolutePath());							
											
											if(scheduleStatus != 0)
											{
												String scheduledAtConvert = scheduledAt;
												DateFormat formatter ; 
												Date scheduledDate ; 
												formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												scheduledDate = formatter.parse(scheduledAtConvert);
												userJobs.setScheduledAt(scheduledDate);
											}
											/*String queuedAtConvert = queuedAt;
											Date queuedAtDate; 					
											queuedAtDate = formatter.parse(queuedAtConvert);	
											userJobs.setQueuedAt(queuedAtDate);*/

											userJobs.setJobStatus(0);
											userJobs.setJobType(jobType);
											userJobs.setDuplicateStatus(duplicateStatus);
											userJobs.setScheduleStatus(scheduleStatus);
											
											/*userJobs.setSendNow(sendNow);*/
											userJobs.setSendRatio(0);
											userJobs.setProductId(productId);
											userJobs.setRoute(routeList.get(0).getRouteId().getSmppName());
											//userJobs.setCompletedAt(completedAtDate);
											int result = userJobsService.saveUserJobs(userJobs,productId,sentMessage,updateNewBalance);
											if(result == 1){
												if(scheduleStatus != 0){
													map.put("message", "SMS Scheduled successfully");
												}else{
													map.put("message", "SMS sent successfully");
												}
												map.put("code", 201);
								    			map.put("status", "Success");
								    										    			
												if(listCheckAutherization.get(0).getDndCheck().equals("Y")){
													map.put("Total Dnd Number", dndNumberList.get(1));
												}
											}
											else{
												map.put("code", 403);
								    			map.put("status", "error");
								    			map.put("message", "Something Going Worng File Is Not Uploaded");
											}
					    				}
					    				else
					    				{
					    					map.put("code", 204);
							    			map.put("status", "error");
							    			map.put("message", "Route is empty");
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
				    				map.put("code", 0);
									map.put("status", "error");
									map.put("message", "Mobile Count is zero please upload correct file");
				    			}
			    			}
			    			else{
			    				map.put("code", 0);
								map.put("status", "error");
								map.put("message", "Please Select Valid File Type");
			    			}
		    			
			    		}
			    	}
			    	else
			    	{
			    		map.put("code", 414);
						map.put("status", "error");
						map.put("message", "Request File Too Large");	
			    	}
				} catch (IOException e) {
					map.put("code", 404);
	    			map.put("status", e.getMessage());
	    			map.put("message", "File Not Upload");
				}
			}
		}
		else
		{
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");	
		}
		return map;

	}	
	//................................Send Group Message Url..................................................
	
	@RequestMapping(value="/saveUserGroupJobs",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>saveUserGroupJobs(@RequestHeader("Authorization") String authorization,
			@RequestBody String jsonString
			) throws IllegalStateException, ParseException, IOException{

		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1 || tokenAuthentication.validateToken(authorization) == 2){
			
			File file=null;
			File fileData = null;
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString, JsonNode.class);
			List<Object> dndNumberList=null;
			List<UserAuthrization> listCheckAutherization=null;
			List<String> groupContactList = smsHelperService.getGroupContact(node.get("groupId").asText(), node.get("userId").asInt());
			//System.out.println("Group size Details"+groupContactList.size());
			if(groupContactList.size() > 0)
			{
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		        long time = cal.getTimeInMillis();
				  try {
					  String fileName = node.get("groupId").asText()+time+node.get("userId").asInt()+".txt";
				      file = new File(uploadUserJobsFile);				       
			            if (!file.exists()) {
			                file.mkdir();
			            }			            
			            fileData = new File(file, fileName);
			            if (!fileData.exists()) {
			            	fileData.createNewFile();
			            }
			        listCheckAutherization = smsHelperService.getUserAuthrizationCheck(node.get("userId").asInt(),node.get("productId").asInt());
					//System.out.println("Authentication Details:- "+listCheckAutherization.get(0).getDndCheck());
					
					if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
					{	 
						   String mobileNumber = String.join(",",groupContactList);
						   dndNumberList = smsHelperService.mobileNumber(mobileNumber);			   
						  // System.out.println("Dnd Filter list:"+dndNumberList.get(0));
						   List<String> sendMobileLis =new ArrayList<>();
						   for(Object b:dndNumberList)
						   {
							   sendMobileLis.add(String.valueOf(b));
						   }
						   /*System.out.println("new list "+sendMobileLis.get(0));*/
						   mobileNumber = String.join(",",sendMobileLis.get(0));
						   mobileNumber = mobileNumber.replaceAll("[\\[\\](){}]","");
						   FileWriter fw = new FileWriter(fileData.getAbsoluteFile());
					        BufferedWriter bw = new BufferedWriter(fw);
					        groupContactList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
					        for(String contact : groupContactList)
					        {
					        	bw.write(contact);
						        bw.newLine();
					        }
					        
					        bw.close();
					       /* System.out.println("fileter mobile number"+groupContactList);*/
					}					
					if(node.get("duplicateStatus").asInt() == 1)
					{						  
					    groupContactList = Global.filterDuplicateNumber(groupContactList);
					    FileWriter fw = new FileWriter(fileData.getAbsoluteFile());
				        BufferedWriter bw = new BufferedWriter(fw);
				        for(String contact : groupContactList)
				        {
				        	bw.write(contact);
					        bw.newLine();
				        }
				        
				        bw.close();
					}					
					if((node.get("duplicateStatus").asInt() != 1) && (listCheckAutherization.get(0).getDndCheck().equals("N")))
					{
						FileWriter fw = new FileWriter(fileData.getAbsoluteFile());
				        BufferedWriter bw = new BufferedWriter(fw);
				        for(String contact : groupContactList)
				        {
				        	bw.write(contact);
					        bw.newLine();
				        }				        
				        bw.close();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				int messageLength = node.get("message").asText().length();
	    		int messageCount = smsHelperService.messageCount(node.get("messageType").asInt(), messageLength);
	    		if(messageCount > 10)
	    		{
	    			map.put("code", 413);
					map.put("status", "error");
					map.put("message", "Message Count Too Large");
	    		}
	    		else
	    		{
	    			/*System.out.println("contact list data:--- "+groupContactList.get(0));
	    			System.out.println("contact list data:--- "+groupContactList.size());*/
	    			if((groupContactList.size() > 0) && !("".equals(groupContactList.get(0))))
	    			{
	    				List<Integer> balance = smsHelperService.getBalance(node.get("userId").asInt(),node.get("productId").asInt());
		    			//System.out.println("User Balnce "+balance.get(0));
		    			int sentMessage = groupContactList.size() * messageCount;
		    			/*System.out.println("User Sent Message "+ sentMessage);*/
		    			if(sentMessage <= balance.get(0))
		    			{
		    				List<UserProduct>routeList= smsHelperService.getRouteDetails(node.get("userId").asInt(),node.get("productId").asInt());
		    				System.out.println("Route Name"+routeList.get(0).getRouteId().getSmppName());
		    				int updateNewBalance = balance.get(0)-sentMessage; 
		    				UserJobs userJobs= new UserJobs();
							userJobs.setUserId(node.get("userId").asInt());
							userJobs.setMessage(node.get("message").asText());
							userJobs.setMessageType(node.get("messageType").asInt());
							userJobs.setMessageLength(messageLength);
							userJobs.setCount(messageCount);
							userJobs.setSender(node.get("sender").asText());
							userJobs.setTotalNumbers(groupContactList.size());
							userJobs.setTotalSent(sentMessage);
							userJobs.setFilename(fileData.getAbsolutePath());
							
							if(node.get("scheduleStatus").asInt() != 0)
							{
								String scheduledAtConvert = node.get("scheduledAt").asText();
								DateFormat formatter ; 
								Date scheduledDate ; 
								formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								scheduledDate = formatter.parse(scheduledAtConvert);
								userJobs.setScheduledAt(scheduledDate);
							}
							/*String queuedAtConvert = queuedAt;
							Date queuedAtDate; 					
							queuedAtDate = formatter.parse(queuedAtConvert);	
							userJobs.setQueuedAt(queuedAtDate);*/

							userJobs.setJobStatus(0);
							userJobs.setJobType(node.get("jobType").asInt());
							userJobs.setDuplicateStatus(node.get("duplicateStatus").asInt());
							userJobs.setScheduleStatus(node.get("scheduleStatus").asInt());
							
							/*userJobs.setSendNow(sendNow);*/
							userJobs.setSendRatio(0);
							userJobs.setProductId(node.get("productId").asInt());
							userJobs.setRoute(routeList.get(0).getRouteId().getSmppName());
							//userJobs.setCompletedAt(completedAtDate);
							int result = userJobsService.saveUserJobs(userJobs,node.get("productId").asInt(),sentMessage,updateNewBalance);
							if(result == 1)
							{
								if(node.get("scheduleStatus").asInt() != 0){
									map.put("message", "SMS Scheduled successfully");
								}else{
									map.put("message", "SMS sent successfully");
								}
								map.put("code", 201);
				    			map.put("status", "Success");
				    			
				    			if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
				    			{
				    				map.put("Total Dnd Number", dndNumberList.get(1));
				    			}
				    			
							}
							else
							{
								map.put("code", 403);
				    			map.put("status", "error");
				    			map.put("message", "Something Going Worng File Is Not Uploaded");
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
	    				map.put("code", 204);
						map.put("status", "error");
						map.put("message", "Your contact conunt is zeor for send sms, may be your contact number is dnd number");
	    			}
	    			
	    		}
			}
			else
			{
				map.put("code", 404);
				map.put("status", "error");
				map.put("message", "No Contact has Found");
			}    		
		}
		else
		{
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");	
		}
		return map;
	}
	
	//................................Send Quick Message Url..................................................
	
	@RequestMapping(value="/sendQuickMessage",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object>sendQuickMessage(@RequestHeader("Authorization") String authorization,
			@RequestBody String jsonString
			) throws IllegalStateException, ParseException, IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 404);
		map.put("message", "Data Not Inserted");
		System.out.println("Start Quick Messaging");
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1 || tokenAuthentication.validateToken(authorization) == 2){
			mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(jsonString,JsonNode.class);
			List<Object> dndNumberList=null;
			List<UserAuthrization> listCheckAutherization=null;
			listCheckAutherization = smsHelperService.getUserAuthrizationCheck(node.get("userId").asInt(),node.get("productId").asInt());
			System.out.println(listCheckAutherization.get(0).getDndCheck());
			String mobileNumber = node.get("mobileNumber").asText();
			if(mobileNumber != "")
			{
				if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
				{	 
					   dndNumberList = smsHelperService.mobileNumber(mobileNumber);			   
					   //System.out.println("Dnd Filter list:"+dndNumberList.get(0));
					   List<String> sendMobileLis =new ArrayList<>();
					   for(Object b:dndNumberList)
					   {
						   sendMobileLis.add(String.valueOf(b));
					   }
					   // System.out.println("new list "+sendMobileLis.get(0));
					   mobileNumber = String.join(",",sendMobileLis.get(0));
					   mobileNumber = mobileNumber.replaceAll("[\\[\\](){}]","");
  
				}				
				//System.out.println(mobileNumber);
				List<String> mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
				//System.out.println("list adta"+mobileList.get(0));
				if(!mobileList.get(0).equals(""))
				{
					if(mobileList.size() <= 1000 && mobileList.size() > 0)
					{
						if(node.get("duplicateStatus").asInt() == 1)
						{
							mobileList = Global.filterDuplicateNumber(mobileList);
						}
						if(node.get("scheduleStatus").asInt() == 1)
						{
							System.out.println("In side If");
							File file=null;
							File fileData = null;
							 try {
								  long millis = new java.util.Date().getTime();
								  System.out.println(millis);
								  String fileName = millis+node.get("userId").asInt()+"quickschedule"+".txt";
								  System.out.println(fileName);
							      file = new File(uploadUserJobsFile);				     
						            if (!file.exists()) {
						                file.mkdir();
						            }			            
						            fileData = new File(file, fileName);
						            if (!fileData.exists()) {
						            	fileData.createNewFile();
						            }

						        FileWriter fw = new FileWriter(fileData.getAbsoluteFile());
						        BufferedWriter bw = new BufferedWriter(fw);
						        
						        for(String contact : mobileList)
						        {
						        	bw.write(contact);
							        bw.newLine();
						        }
						        
						        bw.close();
						        int messageLength = node.get("message").asText().length();
					    		int messageCount = smsHelperService.messageCount(node.get("messageType").asInt(), messageLength);
					    		if(messageCount > 10)
					    		{
					    			map.put("code", 413);
									map.put("status", "error");
									map.put("message", "Message Count Too Large");
					    		}
					    		else
					    		{
					    			List<Integer> balance = smsHelperService.getBalance(node.get("userId").asInt(),node.get("productId").asInt());
					    			System.out.println("User Balnce "+balance.get(0));
					    			int sentMessage = mobileList.size() * messageCount;
					    			System.out.println("User Sent Message "+ sentMessage);
					    			if(sentMessage <= balance.get(0))
					    			{
					    				List<UserProduct>routeList= smsHelperService.getRouteDetails(node.get("userId").asInt(),node.get("productId").asInt());
					    				System.out.println("Route Name"+routeList.get(0).getRouteId().getSmppName());
					    				int updateNewBalance = balance.get(0)-sentMessage; 
					    				UserJobs userJobs= new UserJobs();
										userJobs.setUserId(node.get("userId").asInt());
										userJobs.setMessage(node.get("message").asText());
										userJobs.setMessageType(node.get("messageType").asInt());
										userJobs.setMessageLength(messageLength);
										userJobs.setCount(messageCount);
										userJobs.setSender(node.get("sender").asText());
										userJobs.setTotalNumbers(mobileList.size());
										userJobs.setTotalSent(sentMessage);
										userJobs.setFilename(fileData.getAbsolutePath());
										
										if(node.get("scheduleStatus").asInt() == 1)
										{
											String scheduledAtConvert = node.get("scheduledAt").asText();
											System.out.println(scheduledAtConvert);
											DateFormat formatter ; 
											Date scheduledDate ; 
											formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											scheduledDate = formatter.parse(scheduledAtConvert);
											userJobs.setScheduledAt(scheduledDate);
										}
										/*String queuedAtConvert = queuedAt;
										Date queuedAtDate; 					
										queuedAtDate = formatter.parse(queuedAtConvert);	
										userJobs.setQueuedAt(queuedAtDate);*/

										userJobs.setJobStatus(0);
										userJobs.setJobType(node.get("jobType").asInt());
										userJobs.setDuplicateStatus(node.get("duplicateStatus").asInt());
										userJobs.setScheduleStatus(node.get("scheduleStatus").asInt());
										
										/*userJobs.setSendNow(sendNow);*/
										userJobs.setSendRatio(0);
										userJobs.setProductId(node.get("productId").asInt());
										userJobs.setRoute(routeList.get(0).getRouteId().getSmppName());
										//userJobs.setCompletedAt(completedAtDate);
										int result = userJobsService.saveUserJobs(userJobs,node.get("productId").asInt(),sentMessage,updateNewBalance);
										if(result == 1)
										{	
											if(node.get("scheduleStatus").asInt() != 0){
												map.put("message", "SMS Scheduled successfully");
											}else{
												map.put("message", "SMS sent successfully");
											}
											map.put("code", 201);
							    			map.put("status", "Success");
							    			
							    			if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
							    			{
							    				map.put("Total Dnd Number", dndNumberList.get(1));
							    			}
										}
										else
										{
											map.put("code", 403);
							    			map.put("status", "error");
							    			map.put("message", "Something Going Worng File Is Not Uploaded");
										}
					    			}
					    		}		        
								
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}

						}
						else
						{
							
							
							
							System.out.println("Start direct in dlr_status table");
							int messageLength = node.get("message").asText().length();
				    		int messageCount = smsHelperService.messageCount(node.get("messageType").asInt(), messageLength);
				    		if(messageCount > 10)
				    		{
				    			map.put("code", 413);
								map.put("status", "error");
								map.put("message", "Message Count Too Large");
				    		}
				    		else
				    		{
				    			List<Integer> balance = smsHelperService.getBalance(node.get("userId").asInt(),node.get("productId").asInt());
				    			System.out.println("User Balnce "+balance.get(0));
				    			int sentMessage = mobileList.size() * messageCount;
				    			System.out.println("User Sent Message "+ sentMessage);
				    			if(sentMessage <= balance.get(0))
				    			{
				    				System.out.println(balance.get(0));
				    				List<UserProduct>routeList= smsHelperService.getRouteDetails(node.get("userId").asInt(),node.get("productId").asInt());
				    				System.out.println("Route Name"+routeList.get(0).getRouteId().getSmppName());
				    				int updateNewBalance = balance.get(0)-sentMessage; 
				    				System.out.println(updateNewBalance);
				    				
				    				/*int coding = 0;
				    				if(node.get("messageType").asInt() == 2)
				    				{
				    					coding =2;
				    				}*/
				    				Map<String,Object> mapList = new HashMap<>();
				    				mapList.put("mobileNumber", mobileNumber);
				    				mapList.put("userId", node.get("userId").asInt());
				    				mapList.put("message", node.get("message").asText());
				    				mapList.put("messageType", node.get("messageType").asInt());
				    				mapList.put("messageLength", messageLength);
				    				mapList.put("messageCount", messageCount);
				    				mapList.put("sender", node.get("sender").asText());
				    				mapList.put("sentMessage", sentMessage);
				    				mapList.put("jobType", node.get("jobType").asInt());
				    				mapList.put("productId", node.get("productId").asInt());
				    				mapList.put("updateNewBalance", updateNewBalance);
				    				mapList.put("routeName", routeList.get(0).getRouteId().getSmppName());
				    				/*mapList.put("coding", coding);*/
									int result = userJobsService.sendQuickMessage(mapList);								
									if(result == 1)
									{
										map.put("code", 201);
						    			map.put("status", "Success");
						    			map.put("message", "Send Quick Message Successfully");
						    			if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
						    			{
						    				map.put("totalDndNumber", dndNumberList.get(1));
						    			}
									}
									else
									{
										map.put("code", 403);
						    			map.put("status", "error");
						    			map.put("message", "Something Going Worng File Is Not Uploaded");
									}
				    			}
				    		}
						}
					}
					else
					{
						map.put("code", 413);
						map.put("status", "error");
						map.put("message", "You sending more then 1000 number");
					}
				}
				else
				{
					map.put("code", 413);
					map.put("status", "error");
					map.put("message", "Your mobile count is zero, might be your number is dnd..");
				}
				
			}
			else
			{
				map.put("code", 413);
				map.put("status", "error");
				map.put("message", "Please send at least one mobile number");
			}
		}else
		{
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "You are not authorization persion");
		}

		return map;
	}
	@RequestMapping(value="/savePersonalizedSms",method = RequestMethod.POST)
	public Map<String,Object>savePersonalizedSms(@RequestHeader("Authorization") String authorization,@RequestParam("file")MultipartFile multipartFile,
			@RequestParam("userId")int userId,@RequestParam("message")String message,
			@RequestParam("messageType")int messageType,@RequestParam("sender")String sender,
			@RequestParam("productId")int productId,
			@RequestParam("scheduledAt")String scheduledAt,			
			@RequestParam("jobType")int jobType,
			@RequestParam("duplicateStatus")int duplicateStatus,
			@RequestParam("scheduleStatus")int scheduleStatus,
			@RequestParam("mobileIndex")String mobileIndex
			) throws IllegalStateException, ParseException, IOException{
		
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1 || tokenAuthentication.validateToken(authorization) == 2 ){
			
			String fileName = "";
			if(!multipartFile.isEmpty()){
				
			    try {
			    	List<Object> dndNumberList=null;
			    	List<UserAuthrization> listCheckAutherization = null;
			    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			        long time = cal.getTimeInMillis();
			    	fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			    	String newFileName = userId+time+fileName;
			    	System.out.println(multipartFile.getSize());
			    	if(multipartFile.getSize() <= 3000000 )
			    	{
			    		if(multipartFile.getOriginalFilename().endsWith(".csv")){
			    			System.out.println("Upload Csv File Details");
			    			String fileUploadDirectory =  uploadUserJobsFile+"/";						
							userJobFile = new File(fileUploadDirectory);						
					        if (!userJobFile.exists()) {
					            if (!userJobFile.mkdirs()) {
					            	
					            	map.put("code", 403);
					    			map.put("status", "error");
					    			map.put("message", "file Upload Directory has not found");
					            }
					        }
							userJobFile = new File(fileUploadDirectory,newFileName);
							multipartFile.transferTo(userJobFile);
							CSVReader reader = new CSVReader(new FileReader(userJobFile));
							String [] nextLine;
							mobileList = new ArrayList<>();
							String[] message_data = message.split("#");
							
							System.out.println("mobileList: "+mobileList);
				            System.out.println("message: "+message);
				           				            
				           //Delimiter used in CSV file				            
			                final String COMMA_DELIMITER = ",";
			            
			                final String NEW_LINE_SEPARATOR = "\n";
				             
			                FileWriter fileWriter = null;
			                //String newPerFileName = uploadUserJobsFile+"/Personalized-"+userId+time+".csv";
			                String personalizedFileUploadDirectory =  uploadUserJobsFile+"/personalized/";	
			                String newPerFileNameXLX = "Personalized-"+userId+time+".xls";
							File personalizeUserJobFile = new File(personalizedFileUploadDirectory);						
					        if (!personalizeUserJobFile.exists()) {
					            if (!personalizeUserJobFile.mkdirs()) {	          	
					            	map.put("code", 403);
					    			map.put("status", "error");
					    			map.put("message", "file Upload Directory has not found");
					            }
					        }
					        personalizeUserJobFile = new File(personalizedFileUploadDirectory,newPerFileNameXLX);
			                try {
			                	while ((nextLine = reader.readNext()) != null) {
			                		if(nextLine[characterIndex(mobileIndex)].length() == 10){
			                			mobileList.add("91"+nextLine[characterIndex(mobileIndex)]);
			                		}else /*if(nextLine[characterIndex(mobileIndex)].length() == 12)*/{
			                			mobileList.add(nextLine[characterIndex(mobileIndex)]);
			                		}/*else{
			                			map.put("code", 403);
						    			map.put("status", "error");
						    			map.put("message", "Check Your mobile number column");
			                		}*/
			                		
			                	}
			                	listCheckAutherization = smsHelperService.getUserAuthrizationCheck(userId,productId);
								if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
								{	 
									String mobileNumber = String.join(",",mobileList);
									dndNumberList = smsHelperService.mobileNumber(mobileNumber);	
									List<String> sendMobileLis =new ArrayList<>();
									for(Object b:dndNumberList)
									{
										sendMobileLis.add(String.valueOf(b));
									}
									mobileNumber = String.join(",",sendMobileLis.get(0));
									mobileNumber = mobileNumber.replaceAll("[\\[\\](){}]","");
									mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
								}
								
								//System.out.println("dndNumberList: "+mobileList);
								
								CSVReader reader2 = new CSVReader(new FileReader(userJobFile));
								
								String [] nextLine2;
								int total_message_count = 0;
								String sheetName = "Sheet1";//name of sheet
								HSSFWorkbook wb = new HSSFWorkbook();
								HSSFSheet sheet = wb.createSheet(sheetName);
								
								int count_row = 1;
								while ((nextLine2 = reader2.readNext()) != null) {															
									String new_message = "";
									System.out.println("Flag: "+mobileList.contains(nextLine2[characterIndex(mobileIndex)].length() == 10 ? "91"+nextLine2[characterIndex(mobileIndex)] : nextLine2[characterIndex(mobileIndex)] ));
									if(mobileList.contains(nextLine2[characterIndex(mobileIndex)].length() == 10 ? "91"+nextLine2[characterIndex(mobileIndex)] : nextLine2[characterIndex(mobileIndex)] )){
										//System.out.println("apftercsvfile");
										for(int i=0;i< message_data.length;i++){
											//System.out.println("message_data["+i+"]: "+message_data[i]);
											
												
											if(message_data[i].length() > 1){
												new_message += message_data[i];
											}else if(message_data[i].length() == 1){
												//new_message +=  nextLine2[characterIndex(message_data[i])];
												System.out.println(message_data[i]);
												if(message_data[i].equals("A")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("B")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("C")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("D")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("E")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("F")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("G")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("H")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("I")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
												if(message_data[i].equals("J")){
													new_message +=nextLine2[characterIndex(message_data[i])];
													new_message +=" ";
												}
											}
											
										}
										int messageLength = new_message.length();
							    		int messageCount = smsHelperService.messageCount(messageType, messageLength);
							    		if(messageCount > 10)
							    		{
							    			continue;
							    		}else{
							    			//System.out.println("apftercsvfile1");
							    			HSSFRow row = sheet.createRow(count_row);
							    			
							    			//iterating c number of columns
							    			for (int c=0;c < 2; c++ )
							    			{
							    				HSSFCell cell = row.createCell(c);
							    				
							    				if(c == 0){
							    					if(nextLine2[characterIndex(mobileIndex)].length() == 10){
							    						cell.setCellValue("91"+nextLine2[characterIndex(mobileIndex)]);
							    					}else{
							    						cell.setCellValue(nextLine2[characterIndex(mobileIndex)]);
							    					}
							    					
							    				}
							    				
							    				if(c == 1){
							    					cell.setCellValue(new_message);
							    				}
							    				
							    				//cell.setCellValue("Cell "+count_row+" "+c);
							    			}
							    			//System.out.println("apftercsvfile2");
							    			//System.out.println("xlx_filepath"+personalizeUserJobFile.getAbsolutePath());
							    			FileOutputStream fileOut = new FileOutputStream(personalizeUserJobFile);
							    			wb.write(fileOut);
							    			fileOut.flush();
							    			fileOut.close();
							    			
							    			total_message_count = total_message_count+messageCount;		
//						    				fileWriter.append(nextLine2[characterIndex(mobileIndex)]);						    		
//				    		                fileWriter.append(COMMA_DELIMITER);		    		
//				    		                fileWriter.append(new_message);		    		
//				    		                fileWriter.append(NEW_LINE_SEPARATOR);
//				    		                System.out.println("CSV file was created successfully !!!");

							    		}
									}else{
										continue;
									}
									count_row++;
									System.out.println("new_message: "+new_message);
									 
						        } 
								reader2.close();
								
								if((mobileList.size() > 0) && !("".equals(mobileList.get(0))))
				    			{
				    				List<Integer> balance = smsHelperService.getBalance(userId,productId);
					    			int sentMessage = total_message_count;
					    			if(sentMessage <= balance.get(0))
					    			{
					    				List<UserProduct>routeList= smsHelperService.getRouteDetails(userId, productId);
					    				if(routeList.size() > 0)
					    				{
						    				int updateNewBalance = balance.get(0)-sentMessage; 
						    				UserJobs userJobs= new UserJobs();
											userJobs.setUserId(userId);
//											System.out.println("message type :-- "+messageType);
//											if(messageType == 3)
//											{
//												//byte [] b  = message.getBytes("UTF-8");	
//												message = URLEncoder.encode(message, "UTF-8");
//												userJobs.setMessage(message);
//												
//												
//											}
//											else 
//											{
//												userJobs.setMessage(message);
//											}
											userJobs.setMessage(null);
											userJobs.setMessageType(messageType);
											userJobs.setMessageLength(0);
											userJobs.setCount(total_message_count);
											userJobs.setSender(sender);
											userJobs.setTotalNumbers(mobileList.size());
											userJobs.setTotalSent(sentMessage);
											userJobs.setFilename(personalizeUserJobFile.getAbsolutePath());							
											 
											if(scheduleStatus != 0)
											{
												String scheduledAtConvert = scheduledAt;
												DateFormat formatter ; 
												Date scheduledDate ;
												formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												scheduledDate = formatter.parse(scheduledAtConvert);
												userJobs.setScheduledAt(scheduledDate);
											}
											/*String queuedAtConvert = queuedAt;
											Date queuedAtDate; 					
											queuedAtDate = formatter.parse(queuedAtConvert);	
											userJobs.setQueuedAt(queuedAtDate);*/

											userJobs.setJobStatus(0);
											userJobs.setJobType(5);
											userJobs.setDuplicateStatus(duplicateStatus);
											userJobs.setScheduleStatus(scheduleStatus);
											
											/*userJobs.setSendNow(sendNow);*/
											userJobs.setSendRatio(0);
											userJobs.setProductId(productId);
											userJobs.setRoute(routeList.get(0).getRouteId().getSmppName());
											//userJobs.setCompletedAt(completedAtDate);
											int result = userJobsService.saveUserJobs(userJobs,productId,sentMessage,updateNewBalance);
											if(result == 1)
											{
												map.put("code", 201);
								    			map.put("status", "Success");
								    			map.put("message", "file Upload ");							    			
												if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
												{
													map.put("Total Dnd Number", dndNumberList.get(1));
												}
											}
											else
											{
												map.put("code", 403);
								    			map.put("status", "error");
								    			map.put("message", "Something Going Worng File Is Not Uploaded");
											}
					    				}
					    				else
					    				{
					    					map.put("code", 204);
							    			map.put("status", "error");
							    			map.put("message", "Route is empty");
					    				}				    				
					    			}
					    			else
					    			{
					    				map.put("code", 204);
										map.put("status", "error");
										map.put("message", "Insufficieant Balance");
					    			}
				    			}
			                }catch (Exception e) {
			    				
			    	            System.out.println("Error in CsvFileWriter !!!");
			    	            map.put("code", 204);
								map.put("status", "error");
								map.put("message", "Don't select blank column in message box !!!");
			    	            e.printStackTrace();
			    	
			    	        } finally {
			    	
			    	        }
				           reader.close(); 
			    		}
			    		else
			    		{
			    			map.put("code", 415);
			    			map.put("status", "error");
			    			map.put("message", "Unsupported File Format");
			    		}	
			    	}
			    	else
			    	{
			    		map.put("code", 414);
						map.put("status", "error");
						map.put("message", "Request File Too Large");	
			    	}
				} catch (IOException e) {
					map.put("code", 404);
	    			map.put("status", e.getMessage());
	    			map.put("message", "File Not Upload");
				}
			}
		}
		else
		{
			map.put("code", 401);
			map.put("status", "error");
			map.put("message", "user not authorized");	
		}
		return map;	
		
	}
	
	public int characterIndex(String c){
		c = c.toUpperCase();
		int index = 0;
		if(c.equals("A")){
			index =  0;
		}else if(c.equals("B")){
			index =  1;
		}else if(c.equals("C")){
			index =  2;
		}else if(c.equals("D")){
			index =  3;
		}else if(c.equals("E")){
			index =  4;
		}else if(c.equals("F")){
			index =  5;
		}else if(c.equals("G")){
			index =  6;
		}else if(c.equals("H")){
			index =  7;
		}else if(c.equals("I")){
			index =  8;
		}else if(c.equals("J")){
			index =  9;
		}
		
		return index;
	}


}
