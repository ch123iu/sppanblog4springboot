package net.sppan.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sppan.blog.dao.BlogRepository;
import net.sppan.blog.entity.Blog;
import net.sppan.blog.entity.Category;
import net.sppan.blog.service.BlogService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService{

	@Resource
	private BlogRepository blogRepository;
	
	@Override
	public List<Blog> findHotN(int n) {
		Pageable pageable = new PageRequest(0,n);
		return blogRepository.findAllByOrderByViewsDesc(pageable).getContent();
	}

	@Override
	public List<Blog> findFeaturedN(int n) {
		Pageable pageable = new PageRequest(0,n);
		return blogRepository.findByFeaturedOrderByCreateAtDesc(1,pageable).getContent();
	}

	@Override
	public Page<Blog> findByCategoryANDPrivacy(Long categoryId, int privacy,
			PageRequest pageRequest) {
		Category category = new Category();
		category.setId(categoryId);
		return blogRepository.findByCategoryAndPrivacy(category ,privacy,pageRequest);
	}

}
