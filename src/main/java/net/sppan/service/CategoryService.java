package net.sppan.service;

import java.util.List;

import net.sppan.entity.Category;

public interface CategoryService {
	public List<Category> findVisible();
}
