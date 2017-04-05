package net.sppan.blog.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import net.sppan.blog.dao.BlogRepository;
import net.sppan.blog.entity.Blog;
import net.sppan.blog.entity.Category;
import net.sppan.blog.entity.Tag;
import net.sppan.blog.exception.ServiceException;
import net.sppan.blog.service.BlogService;
import net.sppan.blog.service.CategoryService;
import net.sppan.blog.service.TagService;
import net.sppan.blog.utils.HtmlFilter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BlogServiceImpl implements BlogService{

	@Resource
	private BlogRepository blogRepository;
	
	@Resource
	private TagService tagService;
	
	@Resource
	private CategoryService categoryService;
	
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
			Pageable pageable) {
		Category category = new Category();
		category.setId(categoryId);
		return blogRepository.findByCategoryAndPrivacy(category ,privacy,pageable);
	}

	@Override
	public Blog findById(Long id) {
		Blog blog = blogRepository.findOne(id);
		//浏览量增加
		blog.setViews(blog.getViews() + 1);
		blogRepository.saveAndFlush(blog);
		return blog;
	}

	@Override
	public Page<Blog> findAll(Pageable pageable) {
		return blogRepository.findAll(pageable);
	}

	@Override
	public void saveOrUpdate(Blog blog) {
		if(blog == null){
			throw new ServiceException("操作对象不能为空");
		}
		
		if(blog.getId() != null){
			Blog dbBlog = findById(blog.getId());
			dbBlog.setTitle(blog.getTitle());
			dbBlog.setCategory(blog.getCategory());
			dbBlog.setPrivacy(blog.getPrivacy());
			dbBlog.setContent(blog.getContent());
			dbBlog.setSummary(HtmlFilter.truncate(blog.getContent(),300));
			dbBlog.setTags(blog.getTags());
			blogRepository.saveAndFlush(dbBlog);
		}else{
			//设置博客基本属性
			blog.setCreateAt(new Date());
			blog.setFeatured(0);
			blog.setStatus(0);
			blog.setViews(0);
			blog.setSummary(HtmlFilter.truncate(blog.getContent(),300));
			blogRepository.save(blog);
			
		}
		
		//同步标签
		tagService.synBlogTag(blog.getTags());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				//重新统计分类下面的文章数量
				categoryService.countCategoryHasBlog();
				//重新统计标签下面的文章数量
				tagService.countTagHasBlog();
			}
		}).start();
	}

	@Override
	public void change(Long id, String type) {
		Blog blog = findById(id);
		switch (type) {
		case "privacy":
			blog.setPrivacy(blog.getPrivacy() == 0 ? 1 : 0);
			break;
		case "featured":
			blog.setFeatured(blog.getFeatured() == 0 ? 1 : 0);
			break;
		case "status":
			blog.setStatus(blog.getStatus() == 0 ? 1 : 0);
			break;
		default:
			break;
		}
		blogRepository.save(blog);
	}

	@Override
	public void delete(Long id) {
		blogRepository.delete(id);
	}

	@Override
	public Long getBlogCountByCategory(Category category) {
		return blogRepository.countByCategory(category);
	}

	@Override
	public Page<Blog> findByTagName(String tagName,Pageable pageable) {
		return blogRepository.findByTagsContaining(tagName, pageable);
	}

	@Override
	public Long getBlogCountByTag(Tag tag) {
		return blogRepository.countByTagsContaining(tag.getName());
	}
	
}
