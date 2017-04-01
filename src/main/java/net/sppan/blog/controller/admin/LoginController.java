package net.sppan.blog.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sppan.blog.common.JsonResult;
import net.sppan.blog.controller.BaseController;
import net.sppan.blog.entity.Session;
import net.sppan.blog.service.UserService;
import net.sppan.blog.utils.CookieKit;
import net.sppan.blog.utils.IpKit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends BaseController{
	
	@Resource
	private UserService userService;
	/**
	 * 跳转登录页面
	 * @return
	 */
	@GetMapping("/login")
	public String login(){
		return "admin/login";
	}
	
	/**
	 * 登录操作
	 * @param request
	 * @param response
	 * @param username 用户名
	 * @param password 密码 
	 * @param keepLogin 保持登录
	 * @return
	 */
	@ResponseBody
	@PostMapping("/login")
	public JsonResult login(
			HttpServletRequest request,
			HttpServletResponse response,
			String username,
			String password,
			@RequestParam(required = false) Boolean keepLogin
			){
		try {
			String ip = IpKit.getRealIp(request);
			//登录系统
			Session session = userService.login(username,password,keepLogin,ip);
			
			//把sessionID写入cookie
			CookieKit.setSessionId2Cookie(response, session.getSessionId(), ip, true);
			
			return JsonResult.ok().set("returnUrl", "/admin");
		} catch (Exception e) {
			return JsonResult.fail(e.getMessage());
		}
	}
	
	/**
	 * 注销用户
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		String sessionId = CookieKit.getSessionIdFromCookie(request, response);
		userService.logout(sessionId);
		CookieKit.removeSessionIdFromCookie(response);
		return "redirect:/admin";
	}
	
	@GetMapping("/update_form")
	public String updatePWD(){
		return "admin/update_form";
	}
	
	@PostMapping("/updatePwd")
	@ResponseBody
	public JsonResult updatePwd(
			HttpServletRequest request, 
			HttpServletResponse response,
			String oldpassword,
			String password1,
			String password2
			){
		try {
			userService.updatePassword(getLoginUser(), oldpassword, password1, password2);
			CookieKit.removeSessionIdFromCookie(response);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
	}
}
