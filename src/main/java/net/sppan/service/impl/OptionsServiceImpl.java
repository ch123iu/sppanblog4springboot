package net.sppan.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sppan.dao.OptionsRepository;
import net.sppan.entity.Options;
import net.sppan.service.OptionsService;

import org.springframework.stereotype.Service;

@Service
public class OptionsServiceImpl implements OptionsService{
	
	@Resource
	private OptionsRepository optionsRepository;

	@Override
	public List<Options> findAll() {
		return optionsRepository.findAll();
	}

}
