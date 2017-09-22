package com.mobisoft.sms.restcontroller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.FileDataSource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.Route;
import com.mobisoft.sms.model.UserAuthrization;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.model.UserProduct;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.service.UserJobsService;
import com.mobisoft.sms.utility.TokenAuthentication;

import au.com.bytecode.opencsv.CSVReader;
import javassist.compiler.ast.Symbol;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.web.util.TextEscapeUtils;

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
	
	/*@Value("${uploadUserCsvFile}")
	private String uploadCsvTextFile;*/
	
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
			    	fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			    	String newFileName = userId+fileName;
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
							System.out.println("First file Mobile data"+mobileList);
							br.close();
							fr.close();
							listCheckAutherization = smsHelperService.getUserAuthrizationCheck(userId,productId);
							
							System.out.println(listCheckAutherization.get(0).getDndCheck());
							
							if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
							{	 
								  String mobileNumber = String.join(",",mobileList);
								   dndNumberList = smsHelperService.mobileNumber(mobileNumber);			   
								   System.out.println("Dnd Filter list:"+dndNumberList.get(0));
								   List<String> sendMobileLis =new ArrayList<>();
								   for(Object b:dndNumberList)
								   {
									   sendMobileLis.add(String.valueOf(b));
								   }
								   System.out.println("new list "+sendMobileLis.get(0));
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
							        System.out.println("fileter mobile number"+mobileList);
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
							
							if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
							{	 
								  String mobileNumber = String.join(",",mobileList);
								   dndNumberList = smsHelperService.mobileNumber(mobileNumber);			   
								   System.out.println("Dnd Filter list:"+dndNumberList.get(0));
								   List<String> sendMobileLis =new ArrayList<>();
								   for(Object b:dndNumberList)
								   {
									   sendMobileLis.add(String.valueOf(b));
								   }
								   System.out.println("new list "+sendMobileLis.get(0));
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
			    			System.out.println("lis size" +mobileList);
			    			if(!(mobileList.size() == 1) && !("".equals(mobileList.get(0))))
			    			{
			    				List<Integer> balance = smsHelperService.getBalance(userId,productId);
				    			System.out.println("User Balnce "+balance.get(0));
				    			int sentMessage = mobileList.size() * messageCount;
				    			System.out.println("User Sent Message "+ sentMessage);
				    			if(sentMessage <= balance.get(0))
				    			{
				    				List<UserProduct>routeList= smsHelperService.getRouteDetails(userId, productId);
				    				System.out.println("Route Name"+routeList.get(0).getRouteId().getSmppName());
				    				int updateNewBalance = balance.get(0)-sentMessage; 
				    				UserJobs userJobs= new UserJobs();
									userJobs.setUserId(userId);
									userJobs.setMessage(message);
									userJobs.setMessageType(messageType);
									userJobs.setMessageLength(messageLength);
									userJobs.setCount(messageCount);
									userJobs.setSender(sender);
									userJobs.setTotalNumbers(mobileList.size());
									userJobs.setTotalSent(sentMessage);
									userJobs.setFilename(userJobFile.getAbsolutePath());							
									String scheduledAtConvert = scheduledAt;
									DateFormat formatter ; 
									Date scheduledDate ; 
									if(scheduledAtConvert != "")
									{
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
			if(groupContactList.size() > 0)
			{
				  try {
					  String fileName = node.get("groupId").asText()+node.get("userId").asInt()+".txt";
				      file = new File(uploadUserJobsFile);				       
			            if (!file.exists()) {
			                file.mkdir();
			            }
			            
			            fileData = new File(file, fileName);
			            if (!fileData.exists()) {
			            	fileData.createNewFile();
			            }

			        listCheckAutherization = smsHelperService.getUserAuthrizationCheck(node.get("userId").asInt(),node.get("productId").asInt());
					System.out.println(listCheckAutherization.get(0).getDndCheck());
					
					if(listCheckAutherization.get(0).getDndCheck().equals("Y"))
					{	 
						   String mobileNumber = String.join(",",groupContactList);
						   dndNumberList = smsHelperService.mobileNumber(mobileNumber);			   
						   System.out.println("Dnd Filter list:"+dndNumberList.get(0));
						   List<String> sendMobileLis =new ArrayList<>();
						   for(Object b:dndNumberList)
						   {
							   sendMobileLis.add(String.valueOf(b));
						   }
						   System.out.println("new list "+sendMobileLis.get(0));
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
					        System.out.println("fileter mobile number"+groupContactList);

					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
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
	    			if(!(groupContactList.size() == 1) && !("".equals(groupContactList.get(0))))
	    			{
	    				List<Integer> balance = smsHelperService.getBalance(node.get("userId").asInt(),node.get("productId").asInt());
		    			System.out.println("User Balnce "+balance.get(0));
		    			int sentMessage = groupContactList.size() * messageCount;
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
							userJobs.setTotalNumbers(groupContactList.size());
							userJobs.setTotalSent(sentMessage);
							userJobs.setFilename(fileData.getAbsolutePath());
							String scheduledAtConvert = node.get("scheduledAt").asText();
							DateFormat formatter ; 
							Date scheduledDate ; 
							if(scheduledAtConvert != "")
							{
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
							userJobs.setRoute(routeList.get(0).getRouteId().getSmppName());
							//userJobs.setCompletedAt(completedAtDate);
							int result = userJobsService.saveUserJobs(userJobs,node.get("productId").asInt(),sentMessage,updateNewBalance);
							if(result == 1)
							{
								
								map.put("code", 201);
				    			map.put("status", "Success");
				    			map.put("message", "Successfully send data");
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
					   System.out.println("Dnd Filter list:"+dndNumberList.get(0));
					   List<String> sendMobileLis =new ArrayList<>();
					   for(Object b:dndNumberList)
					   {
						   sendMobileLis.add(String.valueOf(b));
					   }
					   System.out.println("new list "+sendMobileLis.get(0));
					   mobileNumber = String.join(",",sendMobileLis.get(0));
					   mobileNumber = mobileNumber.replaceAll("[\\[\\](){}]","");
					  
					  
				}
				//System.out.println(mobileNumber);
				List<String> mobileList = Arrays.asList(mobileNumber.split("\\s*,\\s*"));
				System.out.println("list adta"+mobileList.get(0));
				if(mobileList.size() <= 1000 && mobileList.size() > 0)
				{
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
									String scheduledAtConvert = node.get("scheduledAt").asText();
									System.out.println(scheduledAtConvert);
									DateFormat formatter ; 
									Date scheduledDate ; 
									if(scheduledAtConvert != "")
									{
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
									userJobs.setRoute(routeList.get(0).getRouteId().getSmppName());
									//userJobs.setCompletedAt(completedAtDate);
									int result = userJobsService.saveUserJobs(userJobs,node.get("productId").asInt(),sentMessage,updateNewBalance);
									if(result == 1)
									{
										
										map.put("code", 201);
						    			map.put("status", "Success");
						    			map.put("message", "Send Message Successfully ");
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
	

}
