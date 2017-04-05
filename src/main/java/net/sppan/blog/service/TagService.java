package net.sppan.blog.service;

import java.util.List;

import net.sppan.blog.entity.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TagService {

	/**
	 * 查询所有标签
	 * @return
	 */
	List<Tag> findAll();

	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	Page<Tag> findAll(Pageable pageable);

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	Tag findById(Long id);

	/**
	 * 修改或者新增
	 * @param tag
	 */
	void saveOrUpdate(Tag tag);

	/**
	 * 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 改变状态
	 * @param id
	 */
	void changeStatus(Long id);

	/**
	 * 查询所有标签的名称列表
	 * @return
	 */
	List<String> findAllNameList();
	
	/**
	 * 把所有标签同步到标签表中,标签文章数量只加不减
	 * @param tags
	 */
	public void synBlogTag(String tags);

	void countTagHasBlog();
}
