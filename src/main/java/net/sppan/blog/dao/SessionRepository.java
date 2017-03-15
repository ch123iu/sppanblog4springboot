package net.sppan.blog.dao;

import net.sppan.blog.entity.Session;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

	Session findBySessionId(String sessionId);

}
