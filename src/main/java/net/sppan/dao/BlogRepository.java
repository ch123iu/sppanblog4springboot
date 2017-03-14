package net.sppan.dao;

import java.util.List;

import net.sppan.entity.Blog;
import net.sppan.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long>{

	List<Blog> findByFeatured(int featured, Pageable pageable);

	Page<Blog> findByCategoryAndPrivacy(Category category, int privacy, Pageable pageable);
}
