package net.sppan.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import net.sppan.blog.dao.OptionsRepository;
import net.sppan.blog.entity.Options;
import net.sppan.blog.service.OptionsService;

@Service
@Transactional
public class OptionsServiceImpl implements OptionsService{
	
	public final String SITEABOUTMEKEY = "siteAboutMe";
	public final String SITENAMEKEY = "siteName";
	public final String SITEDESCRIPTIONKEY = "siteDescription";
	public final String SITEDOMAINKEY = "siteDomain";
	public final String SITECHANGYANAPPID = "siteChangyanAppId";
	
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
		Options about = optionsRepository.findByOptionKey(SITEABOUTMEKEY);
		about.setOptionValue(content);
		optionsRepository.save(about);
		
		servletContext.setAttribute(about.getOptionKey(), about.getOptionValue());
	}
}
