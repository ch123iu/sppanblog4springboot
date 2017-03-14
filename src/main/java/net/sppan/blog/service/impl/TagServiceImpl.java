package net.sppan.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sppan.blog.dao.TagRepository;
import net.sppan.blog.entity.Tag;
import net.sppan.blog.service.TagService;

import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService{
	
	@Resource
	private TagRepository tagRepository;

	@Override
	public List<Tag> findAll() {
		return tagRepository.findAll();
	}


}
