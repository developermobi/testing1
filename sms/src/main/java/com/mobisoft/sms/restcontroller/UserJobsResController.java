package com.mobisoft.sms.restcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.sms.model.UserJobs;
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.service.UserJobsService;
import com.mobisoft.sms.utility.TokenAuthentication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;

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
	private String uploadUserTextFile;
	
	@Value("${uploadUserCsvFile}")
	private String uploadCsvTextFile;
	
	@Value("${uploadLiveUrl}")
	private String uploadLiveUrl;
	
	private BufferedReader br = null;
	
	private FileReader fr = null;
	
	private File userJobFile;
	
	private String fileName;
	
	private List<String> mobileList;
	
	String rootPath = System.getProperty("catalina.home");
	
	
	@RequestMapping(value="/saveUserJobs",method = RequestMethod.POST)
	public Map<String,Object>saveUserJobs(@RequestHeader("Authorization") String authorization,@RequestParam("file")MultipartFile multipartFile,
			@RequestParam("userId")int userId,@RequestParam("message")String message,
			@RequestParam("messageType")int messageType,@RequestParam("sender")String sender,
			@RequestParam("productId")int productId,
			@RequestParam("scheduledAt")String scheduledAt,
			@RequestParam("jobStatus")int jobStatus,
			@RequestParam("jobType")int jobType,@RequestParam("columns")int columns,
			@RequestParam("sendNow")String sendNow,@RequestParam("sendRatio")int sendRatio,
			@RequestParam("route")String route) throws IllegalStateException, ParseException, IOException{

		
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1){
			
			String fileName = "";
			if(!multipartFile.isEmpty()){
				
			    try {
			    	
			    	fileName = multipartFile.getOriginalFilename().replace(" ", "-");
			    	String newFileName = userId+fileName;
			    	System.out.println(multipartFile.getSize());
			    	if(multipartFile.getSize() <= 3000000 )
			    	{
			    		if(multipartFile.getOriginalFilename().endsWith(".txt")){
				    		
							String fileUploadDirectory =  uploadUserTextFile+"/";						
							userJobFile = new File(fileUploadDirectory);						
					        if (!userJobFile.exists()) {
					            if (!userJobFile.mkdirs()) {
					            	
					            	map.put("code", 404);
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
								mobileList.add(line);
							}
							System.out.println(mobileList.size());
							br.close();
							fr.close();
						}
			    		else{
			    			System.out.println("Upload Csv File Details");
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
			    			List<Integer> balance = smsHelperService.getBalance(userId,productId);
			    			System.out.println("User Balnce "+balance.get(0));
			    			int sentMessage = mobileList.size() * messageCount;
			    			System.out.println("User Sent Message "+ sentMessage);
			    			if(sentMessage <= balance.get(0))
			    			{
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
								userJobs.setFilename(newFileName);							
								String scheduledAtConvert = scheduledAt;
								DateFormat formatter ; 
								Date scheduledDate ; 
								formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								scheduledDate = formatter.parse(scheduledAtConvert);
								System.out.println(scheduledAt);
								/*String queuedAtConvert = queuedAt;
								Date queuedAtDate; 					
								queuedAtDate = formatter.parse(queuedAtConvert);	
								userJobs.setQueuedAt(queuedAtDate);*/
								userJobs.setScheduledAt(scheduledDate);
								
								userJobs.setJobStatus(jobStatus);
								userJobs.setJobType(jobType);
								userJobs.setColumns(columns);
								userJobs.setSendNow(sendNow);
								userJobs.setSendRatio(sendRatio);
								userJobs.setRoute(route);
								//userJobs.setCompletedAt(completedAtDate);
								int result = userJobsService.saveUserJobs(userJobs,productId,sentMessage,updateNewBalance);
								if(result == 1)
								{
									
									map.put("code", 201);
					    			map.put("status", "Success");
					    			map.put("message", "file Upload ");
								}
								else
								{
									map.put("code", 404);
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
			    	else
			    	{
			    		map.put("code", 413);
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
	

}
