package com.atguigu.scw.webui.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TOrderServiceFeign;
import com.atguigu.scw.webui.vo.req.OrderFormInfoSubmitVo;
import com.atguigu.scw.webui.vo.req.OrderInfoSubmitVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;
import com.atguigu.scw.webui.vo.resp.TOrder;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TOrderController {
	@Autowired
	TOrderServiceFeign orderServiceFeign;

	@RequestMapping("/order/pay")
	public String payOrder(OrderFormInfoSubmitVo vo, HttpSession session) {
		log.debug("提交订单立即支付OrderFormInfoSubmitVo=#################################################################{}",
				vo);

		// 条转页面要查的数据都在这，获得的数据放在request域或者session中，前端再去取

		// 1.下单,小vo封装到大vo
		OrderInfoSubmitVo orderInfoSubmitVo = new OrderInfoSubmitVo();

		// 封装表单数据
		BeanUtils.copyProperties(vo, orderInfoSubmitVo);

		UserRespVo userRespVo = (UserRespVo) session.getAttribute("loginMember");

		// session可能失效
		if (userRespVo == null) {
			return "redirect:/login";
		}

		String accessToken = userRespVo.getAccessToken();

		orderInfoSubmitVo.setAccessToken(accessToken);

		//
		ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo) session.getAttribute("returnPayConfirmVoSession");

		if (returnPayConfirmVo == null) {
			return "redirect:/login";
		}

		orderInfoSubmitVo.setProjectid(returnPayConfirmVo.getProjectId());
		orderInfoSubmitVo.setReturnid(returnPayConfirmVo.getReturnId());
		orderInfoSubmitVo.setRtncount(returnPayConfirmVo.getNum());// 支持的数量，回报比例默认1：1

		AppResponse<TOrder> resp = orderServiceFeign.saveOrder(orderInfoSubmitVo);

		TOrder order = resp.getData();

		log.debug("订单详情#######################################################{}", order);

		// 2.支付
		// TODO 要完成的订单支付

		return "redirect:/member/minecrowdfunding";
	}

}
