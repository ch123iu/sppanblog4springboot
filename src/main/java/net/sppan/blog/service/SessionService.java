package net.sppan.blog.service;

import net.sppan.blog.entity.Session;

public interface SessionService {

	Session findBySessionId(String sessionId);

}
