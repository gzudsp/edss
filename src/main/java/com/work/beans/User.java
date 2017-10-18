package com.work.beans;

public class User {
	private Integer id;
	private String name;
	private String description;
	private String tokenId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", description=" + description + ", tokenId=" + tokenId + "]";
	}
	
	
}
