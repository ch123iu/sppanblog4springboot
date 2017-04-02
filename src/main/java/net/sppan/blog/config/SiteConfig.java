package net.sppan.blog.config;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import net.sppan.blog.entity.Options;
import net.sppan.blog.service.OptionsService;

@Component
public class SiteConfig implements InitializingBean{
	
	@Resource
	private OptionsService optionsService;

	@Resource
	private ServletContext servletContext;

	@Override
	public void afterPropertiesSet() throws Exception {
		//initDB();
		setOptions();
	}

	private void initDB() {
		optionsService.initSiteOptions();
	}
	
	private void setOptions() {
		List<Options> list = optionsService.findAll();
		for (Options options : list) {
			servletContext.setAttribute(options.getOptionKey(), options.getOptionValue());
		}
	}
}
