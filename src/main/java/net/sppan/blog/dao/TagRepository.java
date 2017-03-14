package net.sppan.blog.dao;

import net.sppan.blog.entity.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>{
}
