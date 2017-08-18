package com.mobisoft.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.sms.dao.TemplateDao;
import com.mobisoft.sms.model.Template;

@Service
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	TemplateDao templateDao;

	@Override
	public int saveTemplate(Template template) {
		
		return templateDao.saveTemplate(template);
	}

	@Override
	public List<Template> getTemplate() {
		// TODO Auto-generated method stub
		return templateDao.getTemplate();
	}

	@Override
	public List<Template> getTemplateByUserId(int userId) {
		// TODO Auto-generated method stub
		return templateDao.getTemplateByUserId(userId);
	}

	@Override
	public List<Template> getTemplateById(int templateId) {
		// TODO Auto-generated method stub
		return templateDao.getTemplateById(templateId);
	}

	@Override
	public int updateTemplate(Template template) {
		// TODO Auto-generated method stub
		return templateDao.updateTemplate(template);
	}

	@Override
	public int deleteTemplate(Template template) {
		// TODO Auto-generated method stub
		return templateDao.deleteTemplate(template);
	}

	@Override
	public List<Template> getTemplateByUserIdPaginate(int userId, int start, int limit) {
		// TODO Auto-generated method stub
		return templateDao.getTemplateByUserIdPaginate(userId, start, limit);
	}
}
