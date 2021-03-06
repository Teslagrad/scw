package com.atguigu.scw.project.service.exp.handler;

import org.springframework.stereotype.Component;

import com.atguigu.scw.project.bean.TMember;
import com.atguigu.scw.project.service.MemberServiceFeign;
import com.atguigu.scw.vo.resp.AppResponse;

@Component
public class MemberServiceFeignExceptionHandler implements MemberServiceFeign {

	@Override
	public AppResponse<TMember> getMemberById(Integer id) {
		AppResponse<TMember> resp = AppResponse.fail(null);
		resp.setMsg("远程调用【获取用户服务，发起人信息】失败");

		return resp;
	}

}
