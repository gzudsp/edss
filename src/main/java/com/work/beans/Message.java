package com.work.beans;

public class Message {

	private String status;
	private String info;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "Message [status=" + status + ", info=" + info + "]";
	}

	public void method(){
		System.out.println("111");
	}
}
