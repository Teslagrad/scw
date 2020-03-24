package com.atguigu.scw.webui.service.exp.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.TProjectServiceFeign;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;
import com.atguigu.scw.webui.vo.resp.ProjectVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TProjectServiceFeignExceptionHandler implements TProjectServiceFeign {

	@Override
	public AppResponse<List<ProjectVo>> all() {
		AppResponse resp = AppResponse.fail(null);
		resp.setMsg("请求【热点数据】---------------------------------------------------失败");
		log.debug("请求【热点数据】---------------------------------------------------失败");
		return resp;

	}

	@Override
	public AppResponse<ProjectDetailVo> detailsInfo(Integer projectId) {
		AppResponse resp = AppResponse.fail(null);
		resp.setMsg("请求【项目详情】---------------------------------------------------失败");
		log.debug("请求【项目详情】---------------------------------------------------失败");
		return resp;
	}

}
