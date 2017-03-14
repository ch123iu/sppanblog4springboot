package net.sppan.blog.dao;

import net.sppan.blog.entity.LoginLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

}
