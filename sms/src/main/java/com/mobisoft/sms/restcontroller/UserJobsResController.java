package com.mobisoft.sms.restcontroller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.mobisoft.sms.service.SmsHelperService;
import com.mobisoft.sms.service.UserJobsService;
import com.mobisoft.sms.utility.TokenAuthentication;

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
			@RequestParam("userId")int userId) throws IllegalStateException{
	
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
			//String imagePath = "";
			System.out.println(userId);
			String fileDirectory = "";
			String fileName = "";
			if(!multipartFile.isEmpty()){

				
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
				
				String fileUploadPath = rootPath + "/" + uploadUrl + "/" + fileDirectory + fileName;
				
			    File imageFile = new File(fileUploadPath);
			    System.out.println(imageFile.getAbsolutePath());
			    try {
					multipartFile.transferTo(imageFile);
					
					map.put("code", 404);
	    			map.put("status", "error");
	    			map.put("message", "file Upload ");
				} catch (IOException e) {
					map.put("code", 404);
	    			map.put("status", "error");
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
