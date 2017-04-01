package net.sppan.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import net.sppan.blog.dao.OptionsRepository;
import net.sppan.blog.entity.Options;
import net.sppan.blog.service.OptionsService;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class OptionsServiceImpl implements OptionsService{
	
	private final String about_me_key = "siteAboutMe";
	
	@Resource
	private ServletContext servletContext;
	
	@Resource
	private OptionsRepository optionsRepository;

	@Override
	public List<Options> findAll() {
		return optionsRepository.findAll();
	}

	@Override
	public void saveAboutMe(String content) {
		Options about = optionsRepository.findByOptionKey(about_me_key);
		about.setOptionValue(content);
		optionsRepository.save(about);
		
		servletContext.setAttribute(about.getOptionKey(), about.getOptionValue());
	}

}
