package net.sppan.blog.dao;

import java.util.List;

import net.sppan.blog.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	List<Category> findByStatus(int i);

}
