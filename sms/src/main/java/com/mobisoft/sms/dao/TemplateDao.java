package com.mobisoft.sms.dao;

import java.util.List;

import com.mobisoft.sms.model.Template;


public interface TemplateDao {

	public int saveTemplate(Template template);
	
	public List<Template> getTemplate();
	
	public List<Template> getTemplateByUserId(int userId);
	
	public List<Template> getTemplateByUserIdPaginate(int userId,int start,int limit);
	
	public List<Template> getTemplateById(int templateId);	
	
	public int updateTemplate(Template template);
	
	public int deleteTemplate(Template template);
}
