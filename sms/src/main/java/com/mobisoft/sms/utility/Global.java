package com.mobisoft.sms.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Global {

	public static String randomString(int length) {
	    StringBuffer buffer = new StringBuffer();
	    while (buffer.length() < length) {
	        buffer.append(uuidString());
	    }

	    //this part controls the length of the returned string
	    return buffer.substring(0, length);  
	}
	private static String uuidString() {
	    return UUID.randomUUID().toString().replaceAll("-", "");
	}
	public static String removeAccents(String text) {
		
		  return text == null ? null :
		    Normalizer.normalize(text, Form.NFD)
		        .replaceAll("[^A-Za-z ]", "").toUpperCase();
		}
	
	public static int sendMessage(String userName, String password,String mobile,String senderId,String message) throws MalformedURLException{
		String smsUrl="http://makemysms.in/api/sendsms.php?username="+userName+"&password="+password+"&sender="+senderId+"&mobile=91"+mobile+"&type=1&message="+URLEncoder.encode(message);
		
			URL url = new URL(smsUrl);
			int temp=0;
			BufferedReader reader = null;
			try
			{
				System.out.println(url);				
				reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
				String line1="";
				for (String line; (line = reader.readLine()) != null;)
				{
					System.out.println("Message Details11"+line);					
					line1= line;
				}
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readValue(line1, JsonNode.class);
				System.out.println(node.get("msg").asText());
				if(node.get("code").asText().equals("1"))
				{
					return temp=1;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finally
			{
				if (reader != null) try { reader.close(); } catch (IOException ignore) {}
			}
			//System.out.println("Msg="+msg);
			System.out.println("Msg Url="+smsUrl);
		return temp;
	}
	
}
