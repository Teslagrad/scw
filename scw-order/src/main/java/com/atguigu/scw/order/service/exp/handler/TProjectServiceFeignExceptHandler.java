package com.atguigu.scw.order.service.exp.handler;

import org.springframework.stereotype.Component;

import com.atguigu.scw.order.service.TProjectServiceFeign;
import com.atguigu.scw.order.vo.resp.TReturn;
import com.atguigu.scw.vo.resp.AppResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TProjectServiceFeignExceptHandler implements TProjectServiceFeign {

	@Override
	public AppResponse<TReturn> returnInfo(Integer returnId) {
		AppResponse<TReturn> resp = AppResponse.fail(null);
		resp.setMsg("调用远程服务【项目服务，获取回报】异常######################");
		log.debug("调用远程服务【项目服务，获取回报】异常######################");
		return null;
	}

}
