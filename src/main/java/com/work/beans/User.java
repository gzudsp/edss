package com.work.beans;

import org.apache.http.NameValuePair;

public class User implements NameValuePair{
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
	public User(Integer id, String name, String description, String tokenId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.tokenId = tokenId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
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
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	public void method(){
		
	}
	
}
