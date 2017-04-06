package net.sppan.blog.repository;

import net.sppan.blog.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String userName);

	Page<User> findAllByOrderByCreateAtDesc(Pageable pageable);

}
