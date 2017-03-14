package net.sppan.blog.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sppan.blog.common.JsonResult;
import net.sppan.blog.entity.Session;
import net.sppan.blog.service.UserService;
import net.sppan.blog.utils.CookieKit;
import net.sppan.blog.utils.IpKit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {
	
	@Resource
	private UserService userService;
	/**
	 * 跳转登录页面
	 * @return
	 */
	@RequestMapping(value = {"/login"},method=RequestMethod.GET)
	public String login(){
		return "admin/login";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/login"},method=RequestMethod.POST)
	public JsonResult login(
			HttpServletRequest request,
			HttpServletResponse response,
			String userName,
			String password
			){
		try {
			String ip = IpKit.getRealIp(request);
			//登录系统
			Session session = userService.login(userName,password,true,ip);
			
			//把sessionID写入cookie
			CookieKit.setSessionId2Cookie(response, session.getSessionId(), ip, true);
			
			return JsonResult.success("登录成功");
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
	}

	@RequestMapping(value = { "/", "/index" })
	public String index(){
		return "admin/index";
	}
	
	@RequestMapping("/welcome")
	public String welcome(){
		return "admin/welcome";
	}
}
