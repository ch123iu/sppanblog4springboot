package net.sppan.blog.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import net.sppan.blog.common.Constat;
import net.sppan.blog.entity.LoginLog;
import net.sppan.blog.entity.Session;
import net.sppan.blog.entity.User;
import net.sppan.blog.exception.ServiceException;
import net.sppan.blog.repository.LoginLogRepository;
import net.sppan.blog.repository.SessionRepository;
import net.sppan.blog.repository.UserRepository;
import net.sppan.blog.service.UserService;
import net.sppan.blog.utils.CacheKit;
import net.sppan.blog.utils.MD5Kit;
import net.sppan.blog.utils.StrKit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private LoginLogRepository loginLogRepository;
	
	@Resource
	private SessionRepository sessionRepository;
	
	@Resource
	private CacheKit cacheKit;
	
	@Override
	public Session login(String userName, String password, Boolean keepLogin, String ip) throws ServiceException {
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
			throw new ServiceException("用户名或者密码不能为空");
		}
		User user = userRepository.findByUserName(userName);
		if(user == null){
			throw new ServiceException("用户不存在");
		}
		String passwordMD5 = MD5Kit.generatePasswordMD5(password, user.getSalt());
		if(passwordMD5 != null && passwordMD5.equalsIgnoreCase(user.getPassword())){
			// 如果用户勾选保持登录，暂定过期时间为 3 年，否则为 120 分钟，单位为秒
			long liveSeconds =  keepLogin!= null && keepLogin ? 3 * 365 * 24 * 60 * 60 : 120 * 60;
			// expireAt 用于设置 session 的过期时间点，需要转换成毫秒
			long expireAt = System.currentTimeMillis() + (liveSeconds * 1000);
			// 保存登录 session 到数据库
			Session session = new Session();
			String sessionId = StrKit.getRandomUUID();
			session.setSessionId(sessionId);
			session.setUser(user);
			session.setExpireAt(expireAt);
			sessionRepository.save(session);
			
			cacheKit.put(Constat.CACHE_LOGINUSER, sessionId, user);
			
			//添加登录日志
			loginLogRepository.save(new LoginLog(user,new Date(),ip));
			
			return session;
		}else{
			throw new ServiceException("用户名或者密码不正确");
		}
	}

	@Override
	public Page<User> findAll(PageRequest pageRequest) {
		return userRepository.findAllByOrderByCreateAtDesc(pageRequest);
	}

	@Override
	public void logout(String sessionId) {
		cacheKit.remove(Constat.CACHE_LOGINUSER, sessionId);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public void saveOrUpdate(User user) throws ServiceException{
		if(user == null){
			throw new ServiceException("用户不能为空");
		}
		user.setCreateAt(new Date());
		userRepository.saveAndFlush(user);
	}

	@Override
	public void delete(Long id) {
		if(id == null){
			throw new ServiceException("主键不能为空");
		}
		userRepository.delete(id);
	}

	@Override
	public void updatePassword(User user, String oldpassword, String password1,
			String password2) {
		if(StrKit.isBlank(oldpassword) || StrKit.isBlank(password1) || StrKit.isBlank(password2)){
			throw new ServiceException("参数不完整");
		}
		
		if(!password1.equals(password2)){
			throw new ServiceException("两次输入密码不一致");
		}
		
		User dbUser = findById(user.getId());
		String passwordMD5 = MD5Kit.generatePasswordMD5(password1, user.getSalt());
		if(!user.getPassword().equals(passwordMD5)){
			throw new ServiceException("旧密码不正确");
		}
		dbUser.setPassword(passwordMD5);
		userRepository.saveAndFlush(dbUser);
	}

}
