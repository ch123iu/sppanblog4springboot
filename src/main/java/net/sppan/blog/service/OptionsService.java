package net.sppan.blog.service;

import java.util.List;

import net.sppan.blog.entity.Options;



public interface OptionsService {

	List<Options> findAll();

	/**
	 * 设置关于我
	 * @param content
	 */
	void saveAboutMe(String content);

}
