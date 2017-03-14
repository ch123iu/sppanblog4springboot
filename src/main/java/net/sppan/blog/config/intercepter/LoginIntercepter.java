package net.sppan.blog.config.intercepter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sppan.blog.common.Constat;
import net.sppan.blog.utils.CacheKit;
import net.sppan.blog.utils.CookieKit;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author SPPan
 * 
 * 登录检查拦截器
 *
 */
@Component
public class LoginIntercepter implements HandlerInterceptor{
	
	@Resource
	private CacheKit cacheKit;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//从cookie中获取当前用户的sessionId
		String sessionId = CookieKit.getSessionIdFromCookie(request, response);
		//如果获取的session不为空，证明浏览器端有登录记录
		if (sessionId != null) {
			//从登录缓存中取当前的登录用户
			Object object = cacheKit.get(Constat.cache_loginUser, sessionId);
			if (object != null) {
				request.setAttribute("loginUser", object);
				return true;
			}
		}
		//删除无用cookie
		CookieKit.removeSessionIdFromCookie(response);
		//跳转到登录页面
		response.sendRedirect("/admin/login");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
