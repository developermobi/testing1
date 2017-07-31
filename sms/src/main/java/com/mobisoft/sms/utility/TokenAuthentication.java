package com.mobisoft.sms.utility;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.UserService;

@Service("tokenAuthentication")
public class TokenAuthentication {
	
@Autowired
private UserService userService;
	
	public String getToken(String userName, String password){
		
		String plainClientCredentials= userName+":"+password;
		
		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
		
		return base64ClientCredentials;
	}


	public int validateToken(String authorization){
		
	int flag = 0;
	
	if (authorization != null && authorization.startsWith("Basic")) {
	    // Authorization: Basic base64credentials
	    String base64Credentials = authorization.substring("Basic".length()).trim();
	    
	    String credentials = new String(Base64.decodeBase64(base64Credentials));
	   
	    // credentials = username:password
	    String[] values = credentials.split(":");
	    
	    if(values.length == 2){
	    	String userName = values[0];
	        String password = values[1];
	        
	      //  System.out.println("userName: "+userName);
	        //System.out.println("password: "+password);
	       
	        List<User> userList = userService.getUserByUserName(userName);
	        //System.out.println(userList.get(0).getUserName());
			if(userList.size() > 0){
				if(userList.get(0).getUserName().equals(userName) && userList.get(0).getPassword().equals(password) && userList.get(0).getRole()==1){
					flag = 1;	
				}
				else if (userList.get(0).getUserName().equals(userName) && userList.get(0).getPassword().equals(password)) {
					flag = 2;
				}
			
				
			}
	    }
	    
	 }
	
		return flag;
	}
	
}
