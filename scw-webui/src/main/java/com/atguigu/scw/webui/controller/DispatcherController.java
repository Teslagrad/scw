package com.atguigu.scw.webui.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TMemberServiceFeign;
import com.atguigu.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DispatcherController {

	@Autowired
	TMemberServiceFeign memberServiceFeign;

//	@Autowired
//	TProjectServiceFeign projectServiceFeign;

//	@Autowired
//	RedisTemplate redisTemplate;

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

		return "redirect:/index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
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

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

}
