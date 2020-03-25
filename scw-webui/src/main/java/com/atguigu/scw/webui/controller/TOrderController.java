package com.atguigu.scw.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.webui.vo.req.OrderFormInfoSubmitVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TOrderController {

	@RequestMapping("/order/pay")
	public String payOrder(OrderFormInfoSubmitVo vo) {
		log.debug("提交订单立即支付OrderFormInfoSubmitVo=#################################################################{}",
				vo);

		// 条转页面要查的数据都在这，获得的数据放在request域或者session中，前端再去取

		return "redirect:/member/minecrowdfunding";
	}

}
