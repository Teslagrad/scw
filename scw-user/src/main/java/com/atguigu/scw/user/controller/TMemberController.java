package com.atguigu.scw.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.bean.TMemberAddress;
import com.atguigu.scw.user.service.TMemberService;
import com.atguigu.scw.user.vo.resp.UserAddressVo;
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
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@ApiOperation(value = "查询会员的信息")
	@ApiImplicitParams(value = { @ApiImplicitParam(value = "会员ID", name = "id", required = true) })
	@GetMapping("/getMemberById")
	public AppResponse<TMember> getMemberById(@RequestParam("id") Integer id) {

		TMember member = memberService.getMemberById(id);

		return AppResponse.ok(member);

	}

	@ApiOperation(value = "查询会员收货地址")
	@ApiImplicitParams(value = { @ApiImplicitParam(value = "访问令牌", name = "accessToken", required = true) })
	@GetMapping("/address")
	public AppResponse<List<UserAddressVo>> address(String accessToken) {// 1，传过来accessToken

		// 去redis获取会员id
		String memberId = stringRedisTemplate.opsForValue().get(accessToken);

		if (StringUtils.isEmpty(memberId)) {
			return AppResponse.fail(null);
		}

		// 调用servie层获取地址
		List<TMemberAddress> addressList = memberService.ListAddress(Integer.parseInt(memberId));

		// 转换为vo类型
		List<UserAddressVo> addressVoList = new ArrayList<UserAddressVo>();

		for (TMemberAddress tMemberAddress : addressList) {
			UserAddressVo vo = new UserAddressVo();
			vo.setId(tMemberAddress.getId());
			vo.setAddress(tMemberAddress.getAddress());
			addressVoList.add(vo);
		}

		return AppResponse.ok(addressVoList);

	}

}
