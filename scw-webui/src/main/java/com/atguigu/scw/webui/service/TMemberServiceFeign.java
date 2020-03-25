package com.atguigu.scw.webui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.exp.handler.UserFeignFactory;
import com.atguigu.scw.webui.vo.resp.UserAddressVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

@FeignClient(name = "SCW-USER", fallbackFactory = UserFeignFactory.class) // fallback =
																			// TMemberServiceFeignExceptionHandler.class
public interface TMemberServiceFeign {

	@PostMapping("/user/login")
	public AppResponse<UserRespVo> login(@RequestParam("loginacct") String loginacct,
			@RequestParam("password") String password);

	@GetMapping("/user/info/address")
	public AppResponse<List<UserAddressVo>> address(@RequestParam("accessToken") String accessToken);
}
