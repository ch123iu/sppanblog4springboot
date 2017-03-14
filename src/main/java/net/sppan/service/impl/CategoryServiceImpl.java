package net.sppan.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sppan.dao.CategoryRepository;
import net.sppan.entity.Category;
import net.sppan.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Resource
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findVisible() {
		return categoryRepository.findByStatus(0);
	}

}
