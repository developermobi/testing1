package com.mobisoft.sms.utility;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.UUID;

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
}
