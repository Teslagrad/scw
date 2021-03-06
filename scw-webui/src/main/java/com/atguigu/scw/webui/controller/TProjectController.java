package com.atguigu.scw.webui.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMemberServiceFeign;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.req.BaseVo;
import com.atguigu.scw.webui.vo.resp.ProjectBaseInfoVo;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;
import com.atguigu.scw.webui.vo.resp.ProjectRedisStorageVo;
import com.atguigu.scw.webui.vo.resp.ProjectReturnVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;
import com.atguigu.scw.webui.vo.resp.UserAddressVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TProjectController {
	@Autowired
	TProjectServiceFeign projectServiceFeign;
	@Autowired
	TMemberServiceFeign memberServiceFeign;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@RequestMapping("/project/startReturnInfo")
	public String startReturnInfo(ProjectReturnVo vo, HttpSession session) {
		log.debug("###################444444444###############4");
		return "project/start-step-4";
	}

	@RequestMapping("/project/startBaseInfo")
	public String startBaseInfo(ProjectBaseInfoVo vo, HttpSession session) {

		UserRespVo userRespVo = (UserRespVo) session.getAttribute("loginMember");
		if (vo == null) {
			session.setAttribute("preUrl", "/project/startBaseInfo");

			return "redirect:/login";
		}

		String accessToken = userRespVo.getAccessToken();

		vo.setAccessToken(accessToken);

		log.debug("##################################accessToken=={}", accessToken);

		String projectToken = (String) session.getAttribute("projectToken");

		vo.setProjectToken(projectToken);

		log.debug("##################################ProjectBaseInfoVo=={}", vo);

		AppResponse<ProjectRedisStorageVo> resp = projectServiceFeign.baseinfo(vo);
		ProjectRedisStorageVo data = resp.getData();

		log.debug("##################################step-1-data----{}", data);

		return "project/start-step-2";
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping("/project/startInfo")
	public String startInfo(HttpSession session) {

		UserRespVo vo = (UserRespVo) session.getAttribute("loginMember");
		if (vo == null) {
			session.setAttribute("preUrl", "/project/startInfo");

			return "redirect:/login";
		}

		String accessToken = vo.getAccessToken();

		log.debug("##################################accessToken=={}", accessToken);

		BaseVo baseVo = new BaseVo();
		baseVo.setAccessToken(accessToken);

		AppResponse<ProjectRedisStorageVo> resp = projectServiceFeign.init(baseVo);
		ProjectRedisStorageVo data = resp.getData();

		String projectToken = data.getProjectToken();
		session.setAttribute("projectToken", projectToken);

		log.debug("##################################ProjectRedisStorageVo=={}", data);

		return "project/start-step-1";
	}

	@RequestMapping("/project/start")
	public String start() {

		return "project/start";
	}

	// 去结算
	@RequestMapping("/project/confirm/order/{num}")
	public String confirmOrder(@PathVariable("num") Integer num, Model model, HttpSession session) {// @PathVariable占位符过来的参数要加

		// 1.数据展示，地址信息
		UserRespVo vo = (UserRespVo) session.getAttribute("loginMember");

		if (vo == null) {
			session.setAttribute("preUrl", "/project/confirm/order/" + num);

			return "redirect:/login";
		}

		String accessToken = vo.getAccessToken();

		// 远程调用拿地址信息
		AppResponse<List<UserAddressVo>> resp = memberServiceFeign.address(accessToken);
		List<UserAddressVo> list = resp.getData();

		model.addAttribute("memberAddressList", list);

		// 2，数据展示-回报信息
		ReturnPayConfirmVo returnPayConfirmVo = (ReturnPayConfirmVo) session.getAttribute("returnPayConfirmVoSession");

		returnPayConfirmVo.setNum(num);
		returnPayConfirmVo
				.setTotalPrice(new BigDecimal(num * returnPayConfirmVo.getPrice() + returnPayConfirmVo.getFreight()));

		session.setAttribute("returnPayConfirmVoSession", returnPayConfirmVo);

		return "project/pay-step-2";
	}

	// 支持
	@RequestMapping("/project/support/{projectId}/{returnId}")
	public String support(@PathVariable("projectId") Integer projectId, @PathVariable("returnId") Integer returnId,
			Model model, HttpSession session) {
		AppResponse<ReturnPayConfirmVo> resp = projectServiceFeign.returnInfo(projectId, returnId);
		ReturnPayConfirmVo data = resp.getData();

		model.addAttribute("returnPayConfirmVo", data);

		session.setAttribute("returnPayConfirmVoSession", data);

		return "project/pay-step-1";
	}

	@RequestMapping("/project/projectInfo")
	public String index(Integer id, Model model) {

		AppResponse<ProjectDetailVo> resp = projectServiceFeign.detailsInfo(id);
		ProjectDetailVo vo = resp.getData();
		model.addAttribute("projectDetailVo", vo);
		return "project/index";
	}
}
