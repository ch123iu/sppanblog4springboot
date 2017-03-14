package net.sppan.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sppan.blog.dao.CategoryRepository;
import net.sppan.blog.entity.Category;
import net.sppan.blog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Resource
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findVisible() {
		return categoryRepository.findByStatus(0);
	}

}
