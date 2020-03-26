package com.atguigu.scw.webui.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMemberServiceFeign;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.resp.ProjectVo;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DispatcherController {

	@Autowired
	TMemberServiceFeign memberServiceFeign;
	@Autowired
	TProjectServiceFeign projectServiceFeign;
	@Autowired
	RedisTemplate redisTemplate;

//	@RequestMapping("/doRegister")
//	public String doRegister(UserRegistVo vo) {
//		// 调用远程注册服务
//		AppResponse<Object> resp = memberServiceFeign.register(vo);
//		String result = (String) resp.getData();
//
//		if (result == "ok") {
//			return "redirect:/login";
//		} else {
//			return "register";
//		}
//
//	}

	@RequestMapping("/doRegister")
	public String doRegister(String loginacct, String userpswd, String email, String code, String usertype) {
		// 调用远程注册服务
		AppResponse<Object> resp = memberServiceFeign.register(loginacct, userpswd, email, code, usertype);
		String result = (String) resp.getData();

		log.debug("########################################---result={}", result);

		if (result.equals("ok")) {
			return "redirect:/login";
		} else {
			return "register";
		}

	}

	@ResponseBody
	@RequestMapping("/sendMsg")
	public String doRegister(String loginacct) {
		// 调用短信服务
		log.debug("拿到手机号########################################{}", login());

		AppResponse<Object> resp = memberServiceFeign.sendsms(loginacct);
		String result = (String) resp.getData();
		log.debug("发送验证码########################################{}", result);
		return result;
	}

	@RequestMapping("/register")
	public String register() {

		return "register";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {

		if (session != null) {
			session.removeAttribute("loginMember");
			session.invalidate();
		}

		return "redirect:/index";
	}

	@RequestMapping("/doLogin")
	public String doLogin(String loginacct, String userpswd, HttpSession session) {
		log.debug("/dologin-----------------------------loginacct:{}", loginacct);
		log.debug("/dologin-----------------------------loginacct:{}", userpswd);

		AppResponse<UserRespVo> resp = memberServiceFeign.login(loginacct, userpswd);

		UserRespVo data = resp.getData();

		if (data == null) {
			log.debug("data====================================================null");
			return "login";
		}

		session.setAttribute("loginMember", data);

		String preUrl = (String) session.getAttribute("preUrl");

		if (StringUtils.isEmpty(preUrl)) {
			return "redirect:/index";
		}

		return "redirect:" + preUrl;// 去结算页面
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

//--------------------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/index")
	public String index(Model model) {
		// 每次判断缓存里面是否有热点信息，没有再去取
		// redisTemplate存储的是集合
		List<ProjectVo> data = (List<ProjectVo>) redisTemplate.opsForValue().get("projectInfo");
		if (data == null) {
			// 远程调用拿到热点项目数据
			// 1.获得数据
			AppResponse<List<ProjectVo>> resp = projectServiceFeign.all();
			data = resp.getData();

			redisTemplate.opsForValue().set("projectInfo", data, 1, TimeUnit.HOURS);// 1个小时过期

		}

		// 放到请求域里

		model.addAttribute("projectVoList", data);

		return "index";
	}

	// 如果Controller方法只是跳转页面，不做其他操作。可以配置mvc:view-controller标签。
	// <mvc:view-controller path="/index" view-name="index"/>
//	@RequestMapping("/index")
//	public String index(Model model) {
//
//		List<ProjectVo> data = (List<ProjectVo>) redisTemplate.opsForValue().get("projectInfo");
//
//		if (data == null) {
//			AppResponse<List<ProjectVo>> resp = projectServiceFeign.all();
//			data = resp.getData();
//			redisTemplate.opsForValue().set("projectInfo", data, 1, TimeUnit.HOURS);
//		}
//
//		model.addAttribute("projectVoList", data);
//
//		return "index";
//	}

}
