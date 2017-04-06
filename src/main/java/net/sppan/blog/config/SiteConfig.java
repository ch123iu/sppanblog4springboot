package net.sppan.blog.config;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import net.sppan.blog.entity.Options;
import net.sppan.blog.lucene.SearcherKit;
import net.sppan.blog.service.BlogService;
import net.sppan.blog.service.OptionsService;

@Component
public class SiteConfig implements InitializingBean{
	
	@Resource
	private OptionsService optionsService;

	@Resource
	private ServletContext servletContext;
	
	@Resource
	private SearcherKit searcherKit;
	
	@Resource
	private BlogService blogService;

	@Override
	public void afterPropertiesSet() throws Exception {
		setOptions();
		reloadIndex();
		
	}

	/**
	 * 设置系统基本属性到ServletContext中
	 */
	private void setOptions() {
		List<Options> list = optionsService.findAll();
		for (Options options : list) {
			servletContext.setAttribute(options.getOptionKey(), options.getOptionValue());
		}
	}
	
	/**
	 * 重建所有文章索引
	 */
	private void reloadIndex() {
		searcherKit.reloadIndex(blogService.findAll());
	}
}
