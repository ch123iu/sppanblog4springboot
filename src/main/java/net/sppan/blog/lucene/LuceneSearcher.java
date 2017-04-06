package net.sppan.blog.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.sppan.blog.entity.Blog;
import net.sppan.blog.service.BlogService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Component
public class LuceneSearcher implements ISearcher {

	private static Analyzer analyzer = null;//分词器
    
    public static Logger logger = LoggerFactory.getLogger(LuceneSearcher.class);

    private static Directory directory;
    
    @Value("${lucene.index.path}")
    private String indexPath;
    
    @Resource
    private BlogService blogService;

    @PostConstruct
    public void init() {
        try {
            File indexDir = new File(indexPath);
            if (!indexDir.exists()) {
                indexDir.mkdirs();
            }
            directory = NIOFSDirectory.open(indexDir);
            analyzer = new IKAnalyzer();
        } catch (IOException e) {
            logger.error("init lucene path error",e);
        }
    }
    public static ReentrantLock lock = new ReentrantLock();
    
    public void getCurrentLock() throws InterruptedException {
        boolean _lock = lock.tryLock(300,TimeUnit.SECONDS);
        while(!_lock){
            _lock = lock.tryLock(300,TimeUnit.SECONDS);
        }
    }
    @Override
    public void addBean(Blog blog) {
        IndexWriter writer = null;
        try {
            getCurrentLock();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            writer = new IndexWriter(directory, iwc);
            Document doc = createDoc(blog);
            writer.addDocument(doc);
        } catch (IOException e) {
            logger.error("add bean to lucene error", e);
        } catch (InterruptedException e) {
            logger.error("add bean to lucene error", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("close failed", e);
            }
            lock.unlock();
        }
    }

    @Override
    public void deleteBean(String blogId) {
        IndexWriter writer = null;
        try {
            getCurrentLock();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            writer = new IndexWriter(directory, iwc);
            writer.deleteDocuments(new Term("id", blogId));
        } catch (IOException e) {
            logger.error("delete bean to lucene error,beanId:"+blogId,e);
        } catch (InterruptedException e) {
            logger.error("delete bean to lucene error,beanId:"+blogId,e);
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("close failed", e);
            }
            lock.unlock();
        }
    }
    
    /**
     * 删除所有
     */
    @Override
    public void deleteAllBean() {
    	IndexWriter writer = null;
    	try {
    		getCurrentLock();
    		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
    		writer = new IndexWriter(directory, iwc);
    		writer.deleteAll();
    	} catch (IOException e) {
    		logger.error("delete allBean to lucene error",e);
    	} catch (InterruptedException e) {
    		logger.error("delete allBean to lucene error",e);
    	} finally {
    		try {
    			if(writer!=null) {
    				writer.close();
    			}
    		} catch (IOException e) {
    			logger.error("close failed", e);
    		}
    		lock.unlock();
    	}
    }

    @Override
    public void updateBean(Blog blog) {
        deleteBean(String.valueOf(blog.getId()));
        addBean(blog);

    }

    /**
     * 创建Doc
     * @param blog
     * @return
     */
    private Document createDoc(Blog blog) {
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new TextField("summary", blog.getSummary(), Field.Store.YES));
        doc.add(new TextField("content", blog.getContent(), Field.Store.YES));
        return doc;
    }
    
    /**
     *  转换为blogs
     * @param searcher
     * @param topDocs
     * @return
     * @throws IOException
     */
    private List<Blog> getBlogs(Query query, IndexSearcher searcher, TopDocs topDocs) throws IOException {
        List<Blog> blogs = new ArrayList<Blog>();
        for (ScoreDoc item : topDocs.scoreDocs) {
            Document doc = searcher.doc(item.doc);
            Long id = Long.parseLong(doc.get("id"));
            Blog blog = blogService.findById(id);
            
            //此处要注意，必须使用拷贝，否则对象 为持久态的，后面的设置高亮赋值会修改到数据库数据
            Blog result = new Blog();
    		BeanUtils.copyProperties(blog, result);
    		
    		result.setTitle(setHighlighter(query, doc, "title"));
    		result.setSummary(setHighlighter(query, doc, "summary"));
    		result.setContent(setHighlighter(query, doc, "content"));
            
            blogs.add(result);
        }
        return blogs;
    }
    
    /**
     * 获取Query 对象
     * @param keyword
     * @param module
     * @return
     */
    private Query getQuery(String keyword) {
        try {
            QueryParser queryParser1 = new QueryParser(Version.LUCENE_47, "content", analyzer);
            Query termQuery1 = queryParser1.parse(keyword);
            
            QueryParser queryParser2 = new QueryParser(Version.LUCENE_47, "title", analyzer);
            Query termQuery2 = queryParser2.parse(keyword);
            
            QueryParser queryParser3 = new QueryParser(Version.LUCENE_47, "summary", analyzer);
            Query termQuery3 = queryParser3.parse(keyword);
            
            BooleanQuery booleanClauses = new BooleanQuery();
            booleanClauses.add(new BooleanClause(termQuery1, BooleanClause.Occur.SHOULD));
            booleanClauses.add(new BooleanClause(termQuery2, BooleanClause.Occur.SHOULD));
            booleanClauses.add(new BooleanClause(termQuery3, BooleanClause.Occur.SHOULD));
            
            booleanClauses.setMinimumNumberShouldMatch(1);
            return booleanClauses;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过关键字搜索分页
     * 
     * @param keyword 关键字
     */
    @Override
    public Page<Blog> search(String keyword) {
        try {
            IndexReader aIndexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = null;
            searcher = new IndexSearcher(aIndexReader);
            Query query = getQuery(keyword);
            TopDocs topDocs = searcher.search(query, 50);
            List<Blog> searcherBeans = getBlogs(query, searcher, topDocs);
            
            Page<Blog> searcherBeanPage = new PageImpl<Blog>(searcherBeans,new PageRequest(0,1000), 1000);
            return searcherBeanPage;
        } catch (Exception e) {
        }
        return null;
    }
    
    /**
     * 分页检索
     * @param pageNum 当前页
     * 
     * @param pageSize 每页条数
     * 
     * @param queryString 关键字
     * 
     */
    @Override
    public Page<Blog> search(int pageNum, int pageSize, String queryString) {
        IndexReader aIndexReader = null;
        try {
            aIndexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = null;
            searcher = new IndexSearcher(aIndexReader);
            Query query = getQuery(queryString);
            ScoreDoc lastScoreDoc = getLastScoreDoc(pageNum, pageSize, query, searcher);
            TopDocs topDocs = searcher.searchAfter(lastScoreDoc, query, pageSize);
            List<Blog> searcherBeans = getBlogs(query, searcher, topDocs);
            int totalRow = searchTotalRecord(searcher, query);
            
            Page<Blog> searcherBeanPage = new PageImpl<Blog>(searcherBeans,new PageRequest(pageNum - 1, pageSize), totalRow);
            return searcherBeanPage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据页码和分页大小获取上一次最后一个ScoreDoc
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @param indexSearcher
     * @return
     * @throws IOException
     */
    private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher indexSearcher) throws IOException {
        if (pageIndex == 1) return null;//如果是第一页返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的数量
        TopDocs tds = indexSearcher.search(query, num);
        return tds.scoreDocs[num - 1];
    }

    /**
     * @param query
     * @return
     * @throws IOException
     * @Title: searchTotalRecord
     * @Description: 获取符合条件的总记录数
     */
    public int searchTotalRecord(IndexSearcher searcher, Query query) throws IOException {
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
        if (topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
            return 0;
        }
        ScoreDoc[] docs = topDocs.scoreDocs;
        return docs.length;
    }

    public void reloadIndex(List<Blog> list) {
    	deleteAllBean();
    	for (Blog blog : list) {
            addBean(blog);
        }
    }
    
    /**
     * 高亮设置
     * @param query
     * @param doc
     * @param field
     * @return
     */
    private String setHighlighter(Query query,Document doc,String field){
        try {
            SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
            Highlighter highlighter = new Highlighter(simpleHtmlFormatter,new QueryScorer(query));
            String fieldValue = doc.get(field);
            String highlighterStr = highlighter.getBestFragment(analyzer,field,fieldValue);
            return highlighterStr == null ? fieldValue:highlighterStr;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

}
