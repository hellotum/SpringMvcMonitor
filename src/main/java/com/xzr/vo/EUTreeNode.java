package com.xzr.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * easyui的tree的数据结构
 */
public class EUTreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 当前文件/文件夹的全路径 */
	private String id;
	/** 父文件夹的全路径 */
	private String parentId;
	private String text;
	/** 文件夹的开关状态 open:打开 closed:关闭 */
	private String state;
	/** 自定义参数 */
	private Map<String, String> attributes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
