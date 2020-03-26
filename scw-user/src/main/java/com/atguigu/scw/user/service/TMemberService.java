package com.atguigu.scw.user.service;

import java.util.List;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.bean.TMemberAddress;
import com.atguigu.scw.user.vo.resp.UserRespVo;

public interface TMemberService {

	int saveTMember(String loginacct, String userpswd, String email, String code, String usertype);

	UserRespVo getUserByLogin(String loginacct, String password);

	TMember getMemberById(Integer id);

	List<TMemberAddress> ListAddress(Integer memberId);

}
