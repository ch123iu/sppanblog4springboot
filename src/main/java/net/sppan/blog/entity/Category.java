package net.sppan.blog.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6677454688636838847L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Column(nullable = false,columnDefinition="int default 0")
	private Integer status;

	@Column(nullable = false,columnDefinition="int default 0")
	private Integer count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
