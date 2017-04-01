package net.sppan.blog.dao;

import net.sppan.blog.entity.Blog;
import net.sppan.blog.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long>{

	/**
	 * 获取推荐博客列表
	 * 
	 * @param featured 推荐状态
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByFeaturedOrderByCreateAtDesc(int featured, Pageable pageable);

	/**
	 * 获取博客分页
	 * @param category
	 * @param privacy
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByCategoryAndPrivacy(Category category, int privacy, Pageable pageable);

	/**
	 * 根据浏览数量获取博客
	 * @param pageable
	 * @return
	 */
	Page<Blog> findAllByOrderByViewsDesc(Pageable pageable);
}
