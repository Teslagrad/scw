package com.atguigu.scw.webui.service.exp.handler;

import org.springframework.stereotype.Component;

import com.atguigu.scw.webui.service.TMemberServiceFeign;

import feign.hystrix.FallbackFactory;

@Component
public class UserFeignFactory implements FallbackFactory<TMemberServiceFeign> {

	private final TMemberServiceFeignExceptionHandler memberServiceFeignExceptionHandler;

	public UserFeignFactory(TMemberServiceFeignExceptionHandler memberServiceFeignExceptionHandler) {
		this.memberServiceFeignExceptionHandler = memberServiceFeignExceptionHandler;
	}

	@Override
	public TMemberServiceFeign create(Throwable cause) {
		// 打印下异常
		cause.printStackTrace();
		return memberServiceFeignExceptionHandler;
	}
}