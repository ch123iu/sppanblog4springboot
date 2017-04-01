package net.sppan.blog.service;

import java.util.List;

import net.sppan.blog.entity.Blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public interface BlogService {

	/**
	 * 获取浏览量Top N
	 * @param n
	 * @return
	 */
	List<Blog> findHotN(int n);

	/**
	 * 获取推荐Top N
	 * @param i
	 * @return
	 */
	List<Blog> findFeaturedN(int i);

	/**
	 * 获取分页
	 * @param categoryId 类型ID
	 * @param privacy 权限
	 * @param pageRequest
	 * @return
	 */
	Page<Blog> findByCategoryANDPrivacy(Long categoryId, int privacy, PageRequest pageRequest);

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	Blog findById(Long id);

	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	Page<Blog> findAll(Pageable pageable);

	void saveOrUpdate(Blog blog);

	/**
	 * 改变博客状态
	 * @param id
	 * @param type
	 */
	void change(Long id, String type);
}
