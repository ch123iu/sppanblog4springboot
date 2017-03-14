package net.sppan.blog.controller.front;

import javax.annotation.Resource;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.entity.Blog;
import net.sppan.blog.service.BlogService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BlogController extends BaseController{
	
	@Resource
	private BlogService blogService;
	
	@RequestMapping("/b/{categoryId}/{pageNumber}")
	public String index(
			@PathVariable("categoryId") Long categoryId,
			@PathVariable("pageNumber") Integer pageNumber,
			ModelMap map
			){
		PageRequest pageRequest = new PageRequest(pageNumber - 1, 5);
		Page<Blog> page = blogService.findByCategoryANDPrivacy(categoryId,0,pageRequest);
		map.put("page", page);
		map.put("c", categoryId);
		return "front/blog/index";
	}
}
