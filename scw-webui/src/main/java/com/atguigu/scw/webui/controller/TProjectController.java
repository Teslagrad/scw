package com.atguigu.scw.webui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;

@Controller
public class TProjectController {
	@Autowired
	TProjectServiceFeign projectServiceFeign;

	@RequestMapping("/project/support/{projectId}/{returnId}")
	public String support(Integer projectId, Integer returnId, Model model) {

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
