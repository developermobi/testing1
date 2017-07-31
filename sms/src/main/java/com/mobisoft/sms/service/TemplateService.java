package com.mobisoft.sms.service;

import java.util.List;

import com.mobisoft.sms.model.Template;

public interface TemplateService {

	public int saveTemplate(Template template);
	
	public List<Template> getTemplate();
	
	public List<Template> getTemplateByUserId(int userId);
	
	public List<Template> getTemplateById(int templateId);	
	
	public int updateTemplate(Template template);
	
	public int deleteTemplate(Template template);
}
