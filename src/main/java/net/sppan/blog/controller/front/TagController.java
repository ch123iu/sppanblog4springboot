package net.sppan.blog.controller.front;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sppan.blog.entity.Blog;
import net.sppan.blog.service.BlogService;

@Controller
public class TagController {
	
	@Resource
	private BlogService blogService;

	@RequestMapping("/t/{tagName}")
	public String index(
			@PathVariable String tagName,
			@RequestParam(required = false,defaultValue="1") Integer p,
			ModelMap map 
			){
		map.put("tagName", tagName);
		PageRequest pageRequest = new PageRequest(p - 1, 5);
		Page<Blog> page = blogService.findByTagName(tagName, pageRequest);
		map.put("page", page);
		return "front/tags/index";
	}
}
