package com.atguigu.scw.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.service.TMemberService;
import com.atguigu.scw.vo.resp.AppResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户个人信息模块")
@RequestMapping("/user/info")
@RestController
public class TMemberController {

	@Autowired
	TMemberService memberService;

	@ApiOperation(value = "查询会员的信息")
	@ApiImplicitParams(value = { @ApiImplicitParam(value = "会员ID", name = "id", required = true) })
	@GetMapping("/getMemberById")
	public AppResponse<TMember> getMemberById(@RequestParam("id") Integer id) {

		TMember member = memberService.getMemberById(id);

		return AppResponse.ok(member);

	}
}
