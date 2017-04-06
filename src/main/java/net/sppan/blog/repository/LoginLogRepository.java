package net.sppan.blog.repository;

import net.sppan.blog.entity.LoginLog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

}
