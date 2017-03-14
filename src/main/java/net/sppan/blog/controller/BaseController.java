package net.sppan.blog.controller;

import net.sppan.blog.entity.User;

public class BaseController {
	private User loginUser = null;

	public User getLoginUser() {
		return loginUser;
	}
	
	public boolean isLogin() {
		return getLoginUser() != null;
	}

	public boolean notLogin() {
		return !isLogin();
	}
}
