package com.atguigu.scw.webui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.atguigu.scw.vo.resp.AppResponse;
import com.atguigu.scw.webui.service.exp.handler.TProjectServiceFeignExceptionHandler;
import com.atguigu.scw.webui.vo.req.BaseVo;
import com.atguigu.scw.webui.vo.resp.ProjectBaseInfoVo;
import com.atguigu.scw.webui.vo.resp.ProjectDetailVo;
import com.atguigu.scw.webui.vo.resp.ProjectRedisStorageVo;
import com.atguigu.scw.webui.vo.resp.ProjectVo;
import com.atguigu.scw.webui.vo.resp.ReturnPayConfirmVo;

@FeignClient(value = "SCW-PROJECT", fallback = TProjectServiceFeignExceptionHandler.class)
public interface TProjectServiceFeign {

	@GetMapping("/project/all")
	public AppResponse<List<ProjectVo>> all();

	@GetMapping("/project/details/info/{projectId}")
	public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId);

	@GetMapping("/project/confirm/returns/{projectId}/{returnId}")
	public AppResponse<ReturnPayConfirmVo> returnInfo(@PathVariable("projectId") Integer projectId,
			@PathVariable("returnId") Integer returnId);

	@PostMapping("/project/create/init")
	public AppResponse<ProjectRedisStorageVo> init(@RequestBody BaseVo vo);

	@PostMapping("/project/create/baseinfo")
	public AppResponse<ProjectRedisStorageVo> baseinfo(@RequestBody ProjectBaseInfoVo vo);

}
