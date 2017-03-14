package net.sppan.dao;

import java.util.List;

import net.sppan.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	List<Category> findByStatus(int i);

}
