package com.xzr.vo;

public enum RestStatus {

	SUCCESS(0, "成功"),

	PARAM_NULL(10000, "必填请求参数为空"),
	PWD_ERROR(10001,"密码错误"),
	UPLOAD_FILE_NULL(10002,"上传文件为空"),
	UPLOAD_FILE_FAILURE(10003,"上传文件失败"),
	GET_DISKINFO_EXCEPTION(10004,"获取磁盘信息异常"),
	GET_CPUINFO_EXCEPTION(10005,"获取cpu信息异常"),
	;
	private int code;
	private String message;

	private RestStatus(int code, String message) {
		this.setCode(code);
		this.setMessage(message);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
