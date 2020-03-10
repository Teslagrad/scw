package com.atguigu.scw.user.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.bean.TMemberExample;
import com.atguigu.scw.user.component.SmsTemplate;
import com.atguigu.scw.user.enums.UserExceptionEnum;
import com.atguigu.scw.user.exp.UserException;
import com.atguigu.scw.user.mapper.TMemberMapper;
import com.atguigu.scw.user.service.TMemberService;
import com.atguigu.scw.user.vo.req.UserRegistVo;
import com.atguigu.scw.user.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TMemberServiceImpl implements TMemberService {
	@Autowired
	TMemberMapper memberMapper;

	@Autowired
	SmsTemplate SmsTemplate;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	// @Transactional(propagation = Propagation.REQUIRED, isolation =
	// Isolation.REPEATABLE_READ)//默认runtimes异常回滚
	@Transactional
	@Override
	public int saveTMember(UserRegistVo vo) {
		try {
			// vo属性对拷到do
			TMember member = new TMember();
			BeanUtils.copyProperties(vo, member);
			member.setUsername(vo.getLoginacct());

			String userpswd = vo.getUserpswd();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			member.setUserpswd(encoder.encode(userpswd));

			int i = memberMapper.insertSelective(member);
			log.debug("注册会员成功-{}", member);
			return i;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("注册会员失败-{}", e.getMessage());
			// throw new RuntimeException("保存会员业务逻辑失败");
			throw new UserException(UserExceptionEnum.USER_SAVE_ERROR);
		}
	}

	@Override
	public UserRespVo getUserByLogin(String loginacct, String password) {

		UserRespVo vo = new UserRespVo();

		TMemberExample example = new TMemberExample();

		example.createCriteria().andLoginacctEqualTo(loginacct);

		List<TMember> list = memberMapper.selectByExample(example);

		if (list == null || list.size() == 0) {
			throw new UserException(UserExceptionEnum.USER_UNEXISTS);

		}

		TMember member = list.get(0);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (!encoder.matches(password, member.getUserpswd())) {
			throw new UserException(UserExceptionEnum.USER_PASSWORD_ERROR);
		}

		BeanUtils.copyProperties(member, vo);
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");

		vo.setAccessToken(accessToken);

		stringRedisTemplate.opsForValue().set(accessToken, member.getId().toString());

		return vo;
	}
}
