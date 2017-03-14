package net.sppan.blog.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_session")
public class Session implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 443119757745421828L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String sessionId;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private Long expireAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Long expireAt) {
		this.expireAt = expireAt;
	}

}
