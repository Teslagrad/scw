package com.atguigu.scw.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.atguigu.scw.enums.ProjectStatusEnume;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.component.OssTemplate;
import com.atguigu.scw.project.constant.ProjectConstant;
import com.atguigu.scw.project.service.TProjectService;
import com.atguigu.scw.project.vo.req.BaseVo;
import com.atguigu.scw.project.vo.req.ProjectBaseInfoVo;
import com.atguigu.scw.project.vo.req.ProjectRedisStorageVo;
import com.atguigu.scw.project.vo.req.ProjectReturnVo;
import com.atguigu.scw.vo.resp.AppResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tesla
 *
 *         2020年3月13日 下午5:39:36
 */
@Slf4j
@Api(tags = "项目发起模块")
@RequestMapping("/project/create")
@RestController
public class ProjectCreateController {
	@Autowired
	TProjectService projectService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	OssTemplate ossTemplate;

	@ApiOperation(value = "1-项目初始创建")
	@PostMapping("/init")
	public AppResponse<Object> init(BaseVo vo) {

		try {
			// 校验判断用户token是否为空
			String accessToken = vo.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请求必须提速accessToken");
				return resp;
			}

			String memberId = stringRedisTemplate.opsForValue().get(accessToken);

			// 判断用户token是否存在
			if (StringUtils.isEmpty(memberId)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请先登录");
				return resp;
			}

			// 小vo对拷到大vo
			ProjectRedisStorageVo bigVo = new ProjectRedisStorageVo();

			BeanUtils.copyProperties(vo, bigVo);

			// 生成代表项目的token作为key，大vojson作为值
			String projectToken = UUID.randomUUID().toString().replace("-", "");

			bigVo.setProjectToken(projectToken);

			// vo转为json
			String bigstr = JSON.toJSONString(bigVo);// fastjson

			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken, bigstr);

			log.debug("大vo数据：{}", bigVo);

			return AppResponse.ok(bigVo);// appresonse组件属于jackson组件,可以把apprespone对象转换成json
		} catch (BeansException e) {
			//
			e.printStackTrace();
			log.debug("表单处理失败", e.getMessage());
			return AppResponse.fail(null);
		}
	}

	@ApiOperation(value = "2-项目基本信息")
	@PostMapping("/baseinfo")
	public AppResponse<Object> baseinfo(ProjectBaseInfoVo vo) {

		try {
			// 校验判断用户token是否为空
			String accessToken = vo.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请求必须提速accessToken");
				return resp;
			}

			String memberId = stringRedisTemplate.opsForValue().get(accessToken);

			// 判断用户token是否存在
			if (StringUtils.isEmpty(memberId)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请先登录");
				return resp;
			}

			// 2.拿到大vo，小vo对拷大vo，再放回去
			String bigJson = stringRedisTemplate.opsForValue()
					.get(ProjectConstant.TEMP_PROJECT_PREFIX + vo.getProjectToken());

			// json转vo
			ProjectRedisStorageVo bigVo = JSON.parseObject(bigJson, ProjectRedisStorageVo.class);

			BeanUtils.copyProperties(vo, bigVo);

			bigJson = JSON.toJSONString(bigVo);

			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + vo.getProjectToken(), bigJson);
			log.debug("大vo数据：{}", bigVo);

			return AppResponse.ok(bigVo);
		} catch (Exception e) {
			//
			e.printStackTrace();
			log.debug("表单处理失败", e.getMessage());
			return AppResponse.fail(null);
		}
	}

	@ApiOperation(value = "3-添加项目回报档位")
	@PostMapping("/return")
	public AppResponse<Object> returnDetail(@RequestBody List<ProjectReturnVo> pro) {

		try {
			// 校验判断用户token是否为空
			String accessToken = pro.get(0).getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请求必须提速accessToken");
				return resp;
			}

			String memberId = stringRedisTemplate.opsForValue().get(accessToken);

			// 判断用户token是否存在
			if (StringUtils.isEmpty(memberId)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请先登录");
				return resp;
			}

			// 2.拿到大vo，小vo对拷大vo，再放回去
			String bigJson = stringRedisTemplate.opsForValue()
					.get(ProjectConstant.TEMP_PROJECT_PREFIX + pro.get(0).getProjectToken());// 至少1个

			// json转vo
			ProjectRedisStorageVo bigVo = JSON.parseObject(bigJson, ProjectRedisStorageVo.class);

			// 对拷到集合
			List<TReturn> projectReturns = bigVo.getProjectReturns();

			for (ProjectReturnVo projectReturnVo : pro) {
				TReturn returnObj = new TReturn();
				BeanUtils.copyProperties(projectReturnVo, returnObj);
				projectReturns.add(returnObj);
			}

			bigJson = JSON.toJSONString(bigVo);

			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + pro.get(0).getProjectToken(),
					bigJson);

			log.debug("大vo数据：{}", bigVo);
			return AppResponse.ok(bigVo);
		} catch (Exception e) {
			//
			e.printStackTrace();
			log.debug("表单处理失败", e.getMessage());
			return AppResponse.fail(null);
		}
	}

	@ApiOperation(value = "4-项目提交审核申请")
	@PostMapping("/submit")
	public AppResponse<Object> submit(String accessToken, String projectToken, String ops) {

		try {
			// 校验判断用户token是否为空

			if (StringUtils.isEmpty(accessToken)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请求必须提速accessToken");
				return resp;
			}

			String memberId = stringRedisTemplate.opsForValue().get(accessToken);

			// 判断用户token是否存在
			if (StringUtils.isEmpty(memberId)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请先登录");
				return resp;
			}

			// 判断ops是0还是1
			if ("0".equals(ops)) {// 保存草稿

				projectService.saveProject(accessToken, projectToken, ProjectStatusEnume.DRAFT.getCode());

				log.debug("ops-------------------------------------------------------0");
				return AppResponse.ok("ok");
			} else if ("1".equals(ops)) {// 保存成功
				log.debug("ops-------------------------------------------------------1");
				projectService.saveProject(accessToken, projectToken, ProjectStatusEnume.SUBMIT_AUTH.getCode());

				return AppResponse.ok("ok");
			} else {
				log.debug("请求方式不支持");
				return AppResponse.fail("请求方式不支持");
			}

		} catch (Exception e) {
			//
			e.printStackTrace();
			log.debug("项目操作失败-{}", e.getMessage());
			return AppResponse.fail(null);
		}
	}

	/**
	 * 文件上传表单要求： ①method="post" ②enctype="multipart/form-data" ③type="file"
	 * 
	 * SpringMVC框架集成commons-fileupload和commons-io组件，完成文件上传操作。 SpringMVC提供文件上传解析器。
	 * Controller处理文件上传时，通过MultipartFile接受文件。
	 */
	@ApiOperation(value = "上传图片")
	@PostMapping("/upload")
	public AppResponse<Object> upload(@RequestParam("uploadfile") MultipartFile[] files) {

		try {
			List<String> filepathList = new ArrayList<String>();

			for (MultipartFile multipartFile : files) {
				String filename = multipartFile.getOriginalFilename();
				filename = UUID.randomUUID().toString().replaceAll("-", "") + "_" + filename;// 拼串，保证图片文件名不会重复
				String filepath = ossTemplate.upload(filename, multipartFile.getInputStream());
				filepathList.add(filepath);
			}
			log.debug("上传文件的路径-{}", filepathList);
			return AppResponse.ok(filepathList);// 返回所有图片信息，可以放到隐藏域里
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传文件出现异常");
			return AppResponse.fail(null);
		}
	}

//	@ApiOperation(value = "4--确认项目法人信息")
//	@PostMapping("/confirm/legal")
//	public AppResponse<Object> legal() {
//		return AppResponse.ok("ok");
//	}

//	@ApiOperation(value = "删除项目回报档位")
//	@DeleteMapping("/return")
//	public AppResponse<Object> deleteReturnDetail() {
//		return AppResponse.ok("ok");
//	}

//	@ApiOperation(value = "项目草稿保存")
//	@PostMapping("/tempsave")
//	public AppResponse<Object> tempsave() {
//		return AppResponse.ok("ok");
//	}

}
