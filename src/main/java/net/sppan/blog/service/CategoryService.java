package net.sppan.blog.service;

import java.util.List;

import net.sppan.blog.entity.Category;

public interface CategoryService {
	public List<Category> findVisible();
}
