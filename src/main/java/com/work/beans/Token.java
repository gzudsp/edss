package com.work.beans;

public class Token {
	private Integer id;
	private User user;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Token [id=" + id + ", user=" + user + "]";
	}
}
