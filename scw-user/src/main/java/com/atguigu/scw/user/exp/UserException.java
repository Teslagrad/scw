package com.atguigu.scw.user.exp;

import com.atguigu.scw.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException {

	public UserException() {
	}

	public UserException(UserExceptionEnum enums) {
		super(enums.getMessageString());
	}

}
