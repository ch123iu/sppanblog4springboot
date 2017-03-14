package net.sppan.blog.dao;

import java.util.List;

import net.sppan.blog.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	/**
	 * 根据状态获取分类列表
	 * @param status
	 * @return
	 */
	List<Category> findByStatus(Integer status);

}
