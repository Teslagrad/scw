package com.atguigu.scw.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class UserRespVo {

	@ApiModelProperty("访问令牌，请妥善保管，以后每次请求都要带上")
	// 令牌 ,登录后会分配给用户一个临时令牌，以后对系统的任何访问都必须携带令牌，否则拒绝访问，必须去登录
	private String accessToken;

	private String loginacct;

	private String username;

	private String email;

	private String authstatus = "0";

	private String usertype;

	private String realname;

	private String cardnum;

	private String accttype;

}
