package net.sppan.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.sppan.blog.entity.Session;
import net.sppan.blog.entity.User;

public interface UserService {

	Session login(String userName, String password,Boolean keepLogin, String ip) throws Exception;

	Page<User> findAll(PageRequest pageRequest);

}
