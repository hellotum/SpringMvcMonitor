package com.xzr.vo;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

	/** 默认序列化id */
	private static final long serialVersionUID = 1L;

	private RestStatus restStatus;
	@SuppressWarnings("unused")
	private int code;
	@SuppressWarnings("unused")
	private String rtnMessage;
	
	private T t;

	public BaseResponse() {
		setRestStatus(RestStatus.SUCCESS);
	}
	
	public RestStatus getRestStatus() {
		return restStatus;
	}

	public void setRestStatus(RestStatus restStatus) {
		this.restStatus = restStatus;
		setCode(restStatus.getCode());
		setRtnMessage(restStatus.getMessage());
	}

	public String getRtnMessage() {
		return restStatus.getMessage();
	}

	public int getCode() {
		return restStatus.getCode();
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setRtnMessage(String rtnMessage) {
		this.rtnMessage = rtnMessage;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
	
	
}
