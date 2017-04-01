package net.sppan.blog.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import net.sppan.blog.dao.TagRepository;
import net.sppan.blog.entity.Tag;
import net.sppan.blog.exception.ServiceException;
import net.sppan.blog.service.TagService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TagServiceImpl implements TagService{
	
	@Resource
	private TagRepository tagRepository;

	@Override
	public List<Tag> findAll() {
		return tagRepository.findAll();
	}

	@Override
	public Page<Tag> findAll(Pageable pageable) {
		return tagRepository.findAll(pageable);
	}

	@Override
	public Tag findById(Long id) {
		return tagRepository.findOne(id);
	}

	@Override
	public void saveOrUpdate(Tag tag) {
		if(tag != null){
			if(tag.getId() != null){
				Tag dbTag = findById(tag.getId());
				dbTag.setName(tag.getName());
				dbTag.setStatus(tag.getStatus());
				tagRepository.save(dbTag);
			}else{
				tag.setCount(0);
				tagRepository.saveAndFlush(tag);
			}
		}else{
			throw new ServiceException("保存对象不能为空");
		}
		
	}

	@Override
	public void delete(Long id) {
		tagRepository.delete(id);
	}

	@Override
	public void changeStatus(Long id) {
		if(id == null){
			throw new ServiceException("ID不能为空");
		}
		Tag tag = findById(id);
		tag.setStatus(tag.getStatus() == 0?1:0);
		tagRepository.saveAndFlush(tag);
	}


}
