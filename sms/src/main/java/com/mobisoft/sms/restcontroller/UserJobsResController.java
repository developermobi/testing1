package com.mobisoft.sms.restcontroller;

import java.io.BufferedReader;
import java.io.File;
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

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class UserJobsResController {
	
	@Autowired
	private TokenAuthentication tokenAuthentication;
	
	@Autowired
	private UserJobsService userJobsService;
	
	private ObjectMapper mapper = null;
	
	@Value("${uploadUrl}")
	private String uploadUrl;
	
	@Value("${uploadLiveUrl}")
	private String uploadLiveUrl;
	
	String rootPath = System.getProperty("catalina.home");
	
	@RequestMapping(value="/saveUserJobs",method = RequestMethod.POST)
	public Map<String,Object>saveUserJobs(@RequestHeader("Authorization") String authorization,@RequestParam("file")MultipartFile multipartFile,
			@RequestParam("userId")int userId,@RequestParam("message")String message,
			@RequestParam("messageType")int messageType,@RequestParam("messageLength")int messageLength,
			@RequestParam("count")int count,@RequestParam("sender")String sender,
			@RequestParam("totalNumbers")int totalNumbers,@RequestParam("totalSent")int totalSent,
			@RequestParam("scheduledAt")String scheduledAt,
			@RequestParam("queuedAt")String queuedAt,
			@RequestParam("completedAt")String completedAt,@RequestParam("jobStatus")int jobStatus,
			@RequestParam("jobType")int jobType,@RequestParam("columns")int columns,
			@RequestParam("sendNow")String sendNow,@RequestParam("sendRatio")int sendRatio,
			@RequestParam("route")String route) throws IllegalStateException, ParseException, IOException{

	
		Map<String,Object> map = new HashMap<>();
		map.put("status", "error");
		map.put("code", 400);
		map.put("message", "some error occured");
		map.put("data", null);
		System.out.println("kdljal;sdjksa");
		if(tokenAuthentication.validateToken(authorization) == 0){
			
			map.put("code", 404);
			map.put("status", "error");
			map.put("message", "Invalid User Name Password");
			
		}else if(tokenAuthentication.validateToken(authorization) == 1){
			System.out.println("kdljal;sdjksa");
			//String imagePath = "";
			System.out.println(userId);
			String fileDirectory = "";
			String fileName = "";
			if(!multipartFile.isEmpty()){
				    byte[] bytes = multipartFile.getBytes();
		            Path path = Paths.get("D://temp//" + multipartFile.getOriginalFilename());
		            Files.write(path, bytes);
				System.out.println(multipartFile.getOriginalFilename());
				String fileUploadDirectory = rootPath + "/" + uploadUrl + "/"+ fileDirectory;
				
				File file = new File(fileUploadDirectory);
				System.out.println(file.exists());
		        if (!file.exists()) {
		            if (!file.mkdirs()) {
		            	System.out.println(file.exists());
		            	map.put("code", 404);
		    			map.put("status", "error");
		    			map.put("message", "file Upload Directory has not found");
		            }
		        }
		        
		        fileName = multipartFile.getOriginalFilename().replace(" ", "-");
		        
				//imagePath = uploadLiveUrl + "/"+ fileDirectory  + fileName;
				
				String fileUploadPath = "src/main/resources/UploadUserFile/" + fileName;	
				
			    File imageFile = new File(fileUploadPath);
			    System.out.println(imageFile.getAbsolutePath());
			    try {
					multipartFile.transferTo(imageFile);					
					
					
				   
					System.out.println("a;kd;asdk;las");

					Resource resource = new ClassPathResource(fileUploadPath);
			    	InputStream is = resource.getInputStream();
			          BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
			          String line;
			          while ((line = br.readLine()) != null) {
			             System.out.println(line);
			       	  }
			          br.close();
					
					
				    UserJobs userJobs= new UserJobs();
					userJobs.setUserId(userId);
					userJobs.setMessage(message);
					userJobs.setMessageType(messageType);
					userJobs.setMessageLength(messageLength);
					userJobs.setCount(count);
					userJobs.setSender(sender);
					userJobs.setTotalNumbers(totalNumbers);
					userJobs.setTotalSent(totalSent);
					userJobs.setFilename(fileName);
					
					String scheduledAtConvert = scheduledAt;
					DateFormat formatter ; 
					Date scheduledDate ; 
					formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					scheduledDate = formatter.parse(scheduledAtConvert);
					System.out.println(scheduledAt);
					String queuedAtConvert = queuedAt;
					Date queuedAtDate; 					
					queuedAtDate = formatter.parse(queuedAtConvert);
					
					/*String completedAtConvert = completedAt;
					Date completedAtDate;
					completedAtDate = formatter.parse(completedAtConvert);*/
					
					userJobs.setScheduledAt(scheduledDate);
					userJobs.setQueuedAt(queuedAtDate);
					userJobs.setJobStatus(jobStatus);
					userJobs.setJobType(jobType);
					userJobs.setColumns(columns);
					userJobs.setSendNow(sendNow);
					userJobs.setSendRatio(sendRatio);
					userJobs.setRoute(route);
					//userJobs.setCompletedAt(completedAtDate);
					int result = userJobsService.saveUserJobs(userJobs);
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
