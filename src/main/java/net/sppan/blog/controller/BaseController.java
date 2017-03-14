package net.sppan.blog.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sppan.blog.common.Constat;
import net.sppan.blog.entity.User;
import net.sppan.blog.utils.CacheKit;
import net.sppan.blog.utils.CookieKit;

public class BaseController {
	@Resource
	private HttpServletRequest request;
	@Resource
	private HttpServletResponse response;
	@Resource
	private CacheKit cacheKit;
	private User loginUser = null;

	public User getLoginUser() {
		String sessionId = CookieKit.getSessionIdFromCookie(request, response);
		if (sessionId != null) {
			Object object = cacheKit.get(Constat.cache_loginUser, sessionId);
			if (object != null) {
				loginUser = (User) object;
			}
		}
		return loginUser;
	}
	
	public boolean isLogin() {
		return getLoginUser() != null;
	}

	public boolean notLogin() {
		return !isLogin();
	}
}
