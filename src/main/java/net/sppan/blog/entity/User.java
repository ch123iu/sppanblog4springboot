package net.sppan.blog.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.sppan.blog.common.Constat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1785824516147698045L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nickName;

	private String userName;

	private String password;

	private String salt;

	/**
	 * 状态 <br>
	 * -1锁定<br>
	 * 0未激活<br>
	 * 1激活
	 */
	@Column(nullable = false, columnDefinition = "Integer default 0")
	private Integer status;

	private String avatar;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = Constat.DATETIME_FORMAT, timezone = "GMT+8")
	private Date createAt;

	private String ip;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
