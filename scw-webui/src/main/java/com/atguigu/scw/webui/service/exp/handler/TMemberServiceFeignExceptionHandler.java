package com.atguigu.scw.webui.service.exp.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMemberServiceFeign;
import com.atguigu.scw.webui.vo.resp.UserAddressVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TMemberServiceFeignExceptionHandler implements TMemberServiceFeign {

	@Override
	public AppResponse<UserRespVo> login(String loginacct, String password) {
		AppResponse<UserRespVo> resp = AppResponse.fail(null);
		resp.setMsg("调用远程服务【登录】失败");

		log.error("调用远程服务【登录】失败");

		return resp;
	}

	@Override
	public AppResponse<List<UserAddressVo>> address(String accessToken) {
		AppResponse<List<UserAddressVo>> resp = AppResponse.fail(null);
		resp.setMsg("调用远程【用户服务】【获取地址列表】失败");

		log.error("调用远程【用户服务】【获取地址列表】失败");

		return resp;
	}

	@Override
	public AppResponse<Object> sendsms(String loginacct) {
		AppResponse<Object> resp = AppResponse.fail(null);
		resp.setMsg("调用远程【用户服务】【发送验证码】失败");

		log.error("调用远程【用户服务】【发送验证码】失败");

		return resp;
	}

	@Override
	public AppResponse<Object> register(String loginacct, String userpswd, String email, String code, String usertype) {
		AppResponse<Object> resp = AppResponse.fail(null);
		resp.setMsg("调用远程【用户服务】【用户注册】失败");

		log.error("调用远程【用户服务】【用户注册】失败");

		return resp;
	}

//	@Override
//	public AppResponse<List<UserAddressVo>> address(String accessToken) {
//		AppResponse<List<UserAddressVo>> resp = AppResponse.fail(null);
//		resp.setMsg("调用远程服务【根据用户查询地址】失败");
//		log.error("调用远程服务【根据用户查询地址】失败");
//		return resp;
//	}

}
