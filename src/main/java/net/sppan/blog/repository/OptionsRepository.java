package net.sppan.blog.repository;

import net.sppan.blog.entity.Options;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Long>{

	Options findByOptionKey(String key);
}
