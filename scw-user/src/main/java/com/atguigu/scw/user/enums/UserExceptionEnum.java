package com.atguigu.scw.user.enums;

public enum UserExceptionEnum {

	USER_EXISTS(1, "用户已经存在"), EMAIL_EXISTS(2, "邮箱已经存在"), USER_LOCKED(3, "用户被锁定"), USER_SAVE_ERROR(4, "用户保存失败"),
	USER_UNEXISTS(5, "用户不存在"), USER_PASSWORD_ERROR(6, "密码错误");

	private int code;
	private String messageString;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}

	private UserExceptionEnum(int code, String messageString) {
		this.code = code;
		this.messageString = messageString;
	}

}
