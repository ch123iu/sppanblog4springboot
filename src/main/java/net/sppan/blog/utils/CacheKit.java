package net.sppan.blog.utils;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
@Scope("singleton")
public class CacheKit {
	@Resource
	private CacheManager cacheManager;
	
	private Object locker = new Object();
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}
	
	Cache getOrAddCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			synchronized(locker) {
				cache = cacheManager.getCache(cacheName);
				if (cache == null) {
					cacheManager.addCacheIfAbsent(cacheName);
					cache = cacheManager.getCache(cacheName);
				}
			}
		}
		return cache;
	}
	
	public void put(String cacheName, Object key, Object value) {
		getOrAddCache(cacheName).put(new Element(key, value));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String cacheName, Object key) {
		Element element = getOrAddCache(cacheName).get(key);
		return element != null ? (T)element.getObjectValue() : null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getKeys(String cacheName) {
		return getOrAddCache(cacheName).getKeys();
	}
	
	public void remove(String cacheName, Object key) {
		getOrAddCache(cacheName).remove(key);
	}
	
	public void removeAll(String cacheName) {
		getOrAddCache(cacheName).removeAll();
	}

}
