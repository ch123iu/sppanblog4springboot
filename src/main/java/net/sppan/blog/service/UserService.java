package net.sppan.blog.service;

import net.sppan.blog.entity.Session;

public interface UserService {

	Session login(String userName, String password,Boolean keepLogin, String ip) throws Exception;

}
