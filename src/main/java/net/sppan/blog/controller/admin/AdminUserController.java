package net.sppan.blog.controller.admin;

import javax.annotation.Resource;

import net.sppan.blog.controller.BaseController;
import net.sppan.blog.entity.User;
import net.sppan.blog.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController{
	@Resource
	private UserService userService;
	@RequestMapping(value = {"/","/index"})
	public String index(){
		return "admin/user/index";
	}
	
	@RequestMapping("/list")
	public Page<User> list(){
		PageRequest pageRequest = getPageRequest();
		return userService.findAll(pageRequest);
	}
	

}
