package com.atguigu.scw.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.order.bean.TOrder;
import com.atguigu.scw.order.service.TOrderService;
import com.atguigu.scw.order.vo.req.OrderInfoSubmitVo;
import com.atguigu.scw.vo.resp.AppResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TOrderController {
	@Autowired
	TOrderService orderService;

	@RequestMapping("/order/saveOrder")
	AppResponse<TOrder> saveOrder(@RequestBody OrderInfoSubmitVo vo) {

		try {
			log.debug("保存订单开始={}", vo);

			TOrder order = orderService.saveOrder(vo);// 保存之后主键回填，生成订单号

			AppResponse<TOrder> resp = AppResponse.ok(order);

			return resp;
		} catch (Exception e) {
			//
			e.printStackTrace();
			AppResponse<TOrder> resp = AppResponse.fail(null);
			resp.setMsg("保存订单失败");
			log.debug(
					"保存订单失败------------------------------------------------------------------------------------------------------------");
			return resp;
		}

	}
}
