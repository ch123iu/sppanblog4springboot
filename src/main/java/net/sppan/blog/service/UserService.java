package net.sppan.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.sppan.blog.entity.Session;
import net.sppan.blog.entity.User;
import net.sppan.blog.exception.ServiceException;

public interface UserService {

	/**
	 * 用户登录
	 * @param userName
	 * @param password
	 * @param keepLogin
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	Session login(String userName, String password,Boolean keepLogin, String ip) throws Exception;

	Page<User> findAll(PageRequest pageRequest);

	/**
	 * 用户注销
	 * @param sessionId
	 */
	void logout(String sessionId);

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	User findById(Long id);

	/**
	 * 新增或者更新用户
	 * @param user
	 */
	void saveOrUpdate(User user) throws ServiceException;

	/**
	 * 删除用户
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 修改用户密码
	 * @param user
	 * @param oldpassword
	 * @param password1
	 * @param password2
	 */
	void updatePassword(User user, String oldpassword, String password1, String password2);

}
