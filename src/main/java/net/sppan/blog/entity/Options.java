package net.sppan.blog.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_options")
public class Options implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7370664893843513614L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String option_key;

	private String option_value;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOption_key() {
		return option_key;
	}

	public void setOption_key(String option_key) {
		this.option_key = option_key;
	}

	public String getOption_value() {
		return option_value;
	}

	public void setOption_value(String option_value) {
		this.option_value = option_value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
