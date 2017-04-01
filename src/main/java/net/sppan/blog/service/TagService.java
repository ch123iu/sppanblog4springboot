package net.sppan.blog.service;

import java.util.List;

import net.sppan.blog.entity.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TagService {

	List<Tag> findAll();

	Page<Tag> findAll(Pageable pageable);

	Tag findById(Long id);

	void saveOrUpdate(Tag tag);

	void delete(Long id);

	void changeStatus(Long id);
}
