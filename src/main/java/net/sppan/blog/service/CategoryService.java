package net.sppan.blog.service;

import java.util.List;

import net.sppan.blog.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
	
	/**
	 * 查询可见分类
	 * @return
	 */
	public List<Category> findVisible();

	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	public Page<Category> findAll(Pageable pageable);

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public Category findById(Long id);

	/**
	 * 更新
	 * @param category
	 */
	public void saveOrUpdate(Category category);

	/**
	 * 根据ID删除
	 * @param id
	 */
	public void delete(Long id);

	/**
	 * 显示状态改变
	 * @param id
	 */
	public void changeStatus(Long id);

	public void countCategoryHasBlog();
}
