package net.sppan.blog.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.sppan.blog.entity.Blog;


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
}
