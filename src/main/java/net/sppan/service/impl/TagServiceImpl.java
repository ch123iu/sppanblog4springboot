package net.sppan.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sppan.dao.TagRepository;
import net.sppan.entity.Tag;
import net.sppan.service.TagService;

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
