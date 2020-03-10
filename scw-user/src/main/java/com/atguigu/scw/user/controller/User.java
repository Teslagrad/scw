package com.atguigu.scw.user.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@ApiModel
public class User {

	@ApiModelProperty(value = "用户主键")
	private String name;
	@ApiModelProperty(value = "用户姓名")
	private String email;

}
