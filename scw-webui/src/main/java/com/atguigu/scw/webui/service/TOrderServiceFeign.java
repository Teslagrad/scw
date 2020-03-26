package com.atguigu.scw.webui.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.exp.handler.TOrderServiceFeignExceptionHandler;
import com.atguigu.scw.webui.vo.req.OrderInfoSubmitVo;
import com.atguigu.scw.webui.vo.resp.TOrder;

@FeignClient(value = "SCW-ORDER", fallback = TOrderServiceFeignExceptionHandler.class)
public interface TOrderServiceFeign {

	/**
	 * 远程调用的传递问题 1 简单 @requestParay @pathVariable 复杂对象 @RequestBody
	 *
	 * 2020年3月26日 下午5:36:52
	 */
	@RequestMapping("/order/saveOrder")
	AppResponse<TOrder> saveOrder(@RequestBody OrderInfoSubmitVo vo);

}
