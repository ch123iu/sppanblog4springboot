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
	
	private final String SITEABOUTMEKEY = "siteAboutMe";
	private final String SITENAMEKEY = "siteName";
	private final String SITEDESCRIPTIONKEY = "siteDescription";
	private final String SITEDOMAINKEY = "siteDomain";
	
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

	@Override
	public void initSiteOptions() {
		//关于我
		Options options = new Options();
		options.setOptionKey(SITEABOUTMEKEY);
		options.setOptionValue("关于我");
		options.setDescription("关于我页面内容");
		optionsRepository.save(options);
		//站点名称
		options = new Options();
		options.setOptionKey(SITENAMEKEY);
		options.setOptionValue("SPPanBlog");
		options.setDescription("站点名称");
		optionsRepository.save(options);
		//站点介绍
		options = new Options();
		options.setOptionKey(SITEDESCRIPTIONKEY);
		options.setOptionValue("SPPanBlog博客系统");
		options.setDescription("站点介绍");
		optionsRepository.save(options);
		//站点域名
		options = new Options();
		options.setOptionKey(SITEDOMAINKEY);
		options.setOptionValue("http://localhost");
		options.setDescription("站点域名");
		optionsRepository.save(options);
		
	}

}
