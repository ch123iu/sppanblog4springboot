package net.sppan.blog.config;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.sppan.blog.entity.Options;
import net.sppan.blog.service.OptionsService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;


@Service
public class ServletContextSet  implements InitializingBean{
	
	@Resource
	private OptionsService optionsService;
	
	@Resource
	private ServletContext servletContext;
	
	@Override
    public void afterPropertiesSet() throws Exception {
		List<Options> list = optionsService.findAll();
		for (Options options : list) {
			servletContext.setAttribute(options.getOption_key(), options.getOption_value());
		}
    }
}
