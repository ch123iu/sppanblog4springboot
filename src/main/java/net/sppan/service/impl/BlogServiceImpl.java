package net.sppan.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sppan.dao.BlogRepository;
import net.sppan.entity.Blog;
import net.sppan.entity.Category;
import net.sppan.service.BlogService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService{

	@Resource
	private BlogRepository blogRepository;
	
	@Override
	public List<Blog> findHotN(int n) {
		Pageable pageable = new PageRequest(0,n,new Sort(Sort.Direction.DESC,"views"));
		return blogRepository.findAll(pageable).getContent();
	}

	@Override
	public List<Blog> findFeaturedN(int n) {
		Pageable pageable = new PageRequest(0,n,new Sort(Sort.Direction.DESC,"createAt"));
		return blogRepository.findByFeatured(1,pageable);
	}

	@Override
	public Page<Blog> findByCategoryANDPrivacy(Long categoryId, int privacy,
			PageRequest pageRequest) {
		Category category = new Category();
		category.setId(categoryId);
		return blogRepository.findByCategoryAndPrivacy(category ,privacy,pageRequest);
	}

}
