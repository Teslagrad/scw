package com.atguigu.scw.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.user.component.SmsTemplate;
import com.atguigu.scw.user.service.TMemberService;
import com.atguigu.scw.user.vo.req.UserRegistVo;
import com.atguigu.scw.user.vo.resp.UserRespVo;
import com.atguigu.scw.vo.resp.AppResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "用户登陆注册模块")
@RequestMapping("/user")
@RestController
public class UserLoginRegistController {

	@Autowired
	SmsTemplate smsTemplate;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	TMemberService memberService;

	@ApiOperation(value = "用户登陆")
	@ApiImplicitParams(value = { @ApiImplicitParam(value = "登陆账号", name = "loginacct"),
			@ApiImplicitParam(value = "用户密码", name = "password") })
	@PostMapping("/login")
	public AppResponse<UserRespVo> login(String loginacct, String password) {

		try {
			log.debug("登录表单数据loginacct- {}", loginacct);
			log.debug("登录表单数据password- {}", password);

			UserRespVo vo = memberService.getUserByLogin(loginacct, password);
			log.debug("登录成功-{}", loginacct);
			return AppResponse.ok(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			log.debug("登录失败-{}", loginacct);

			AppResponse resp = AppResponse.fail(null);

			resp.setMsg(e.getMessage());
			return resp;
		}
	}

	@ApiOperation(value = "用户注册")
	@PostMapping("/register")
	public AppResponse<Object> register(UserRegistVo vo) {

		String loginacct = vo.getLoginacct();

		// 表单校验
		if (!StringUtils.isEmpty(loginacct)) {
			// 去redis拿验证码校验
			String code = stringRedisTemplate.opsForValue().get(loginacct);

			if (!StringUtils.isEmpty(code)) {
				// 判断验证码一致
				if (code.equals(vo.getCode())) {
					// 校验账号是否唯一

					// 保存数据
					int i = memberService.saveTMember(vo);
					if (i == 1) {
						stringRedisTemplate.delete(loginacct);// 手动清除缓存
						return AppResponse.ok("ok");
					} else {
						return AppResponse.fail(null);
					}

				} else {
					AppResponse resp = AppResponse.fail(null);
					resp.setMsg("验证码不一致，请重新发送！");
					return resp;
				}

			} else {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("验证码失效，请重新发送！");
				return resp;
			}
		} else {
			AppResponse resp = AppResponse.fail(null);
			resp.setMsg("用户名称不能为空！");
			return resp;
		}

	}

	@ApiOperation(value = "发送短信验证码")
	@PostMapping("/sendsms")
	public AppResponse<Object> sendsms(String loginacct) {

		StringBuilder code = new StringBuilder();
		for (int i = 1; i <= 4; i++) {
			code.append(new Random().nextInt(10));
		}

		Map<String, String> querys = new HashMap<String, String>();
		querys.put("mobile", loginacct);
		querys.put("param", "code:" + code.toString());
		querys.put("tpl_id", "TP1711063");

		smsTemplate.sendSms(querys);

//		stringRedisTemplate.opsForValue().set(loginacct, code.toString());
		// 设置超时
		stringRedisTemplate.opsForValue().set(loginacct, code.toString(), 5, TimeUnit.MINUTES);

		log.debug("发送短信成功-验证码：{}", code.toString());

		return AppResponse.ok("ok");
	}

	@ApiOperation(value = "验证短信验证码")
	@PostMapping("/valide")
	public AppResponse<Object> valide() {
		return AppResponse.ok("ok");
	}

	@ApiOperation(value = "重置密码")
	@PostMapping("/reset")
	public AppResponse<Object> reset() {
		return AppResponse.ok("ok");
	}

}