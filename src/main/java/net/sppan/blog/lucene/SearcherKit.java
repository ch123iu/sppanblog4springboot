package net.sppan.blog.lucene;

import java.util.List;

import net.sppan.blog.entity.Blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SearcherKit {

	@Autowired
	@Qualifier("luceneSearcher")
	private ISearcher mSearcher;

	public void add(Blog blog) {
		checkSearcher();
		mSearcher.addBean(blog);
	}

	public void delete(String blogId) {
		checkSearcher();
		mSearcher.deleteBean(blogId);
	}

	public void update(Blog blog) {
		checkSearcher();
		mSearcher.updateBean(blog);
	}

	/**
	 * 执行搜索
	 * 
	 * @param keyword
	 * @return List<Blog>
	 */
	public Page<Blog> search(String keyword) {
		checkSearcher();
		return mSearcher.search(keyword);
	}

	/**
	 * 分页搜索
	 * @param pageNum 当前页
	 * @param pageSize 每页条数
	 * @param keyword 关键字
	 * @return
	 */
	public Page<Blog> search(int pageNum, int pageSize, String keyword) {
		checkSearcher();
		return mSearcher.search(pageNum, pageSize, keyword);
	}

	/**
	 * 初始化检查
	 */
	public void checkSearcher() {
		if (mSearcher == null) {
			throw new RuntimeException("must init searcher before,please invoke SearchFactory.use() to init.");
		}
	}
	
	/**
	 * 重检索引
	 */
	public void reloadIndex(List<Blog> list){
		checkSearcher();
		mSearcher.reloadIndex(list);
	}

}
