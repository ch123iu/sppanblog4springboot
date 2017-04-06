package net.sppan.blog.lucene;

import java.util.List;

import net.sppan.blog.entity.Blog;

import org.springframework.data.domain.Page;

public interface ISearcher {

	public void init();

	/**
	 * 添加
	 * @param bean
	 */
	public void addBean(Blog bean);

	/**
	 * 根据ID删除
	 * @param beanId
	 */
	public void deleteBean(String beanId);
	
	/**
	 * 删除所有
	 */
	public void deleteAllBean();

	/**
	 * 更新检索文档
	 * @param bean
	 */
	public void updateBean(Blog bean);

	/**
	 * 分页关键字查询
	 * @param keyword
	 * @return
	 */
	public Page<Blog> search(String keyword);

	/**
	 * 分页关键字查询
	 * @param pageNum
	 * @param pageSize
	 * @param queryString
	 * @return
	 */
	public Page<Blog> search(int pageNum, int pageSize, String queryString);
	
	/**
	 *  重建索引
	 * @param list 初始blog列表
	 */
	public void reloadIndex(List<Blog> list);
}
