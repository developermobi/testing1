package com.mobisoft.sms.restcontroller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mobisoft.sms.model.DlrStatus;
import com.mobisoft.sms.model.User;
import com.mobisoft.sms.service.DlrStatusService;


@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class DlrStatusRestController {
	
	@Autowired
	DlrStatusService dlrStatusService;
	
	@RequestMapping(value = "/saveDlrStatus",method = RequestMethod.POST)
	public void saveUser() throws FileNotFoundException, IOException {
		System.out.println("askldjalskdjlasdjlasdjasjd akjsdhklasd adbasd asdas");
		DlrStatus dlrStatus = new DlrStatus();
		int result = dlrStatusService.saveDlrStatus(dlrStatus);
	}
}
