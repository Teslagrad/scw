package com.atguigu.scw.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.scw.project.bean.TProject;
import com.atguigu.scw.project.bean.TProjectImages;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.bean.TTag;
import com.atguigu.scw.project.bean.TType;
import com.atguigu.scw.project.component.OssTemplate;
import com.atguigu.scw.project.service.ProjectInfoService;
import com.atguigu.scw.project.vo.resp.ProjectDetailVo;
import com.atguigu.scw.project.vo.resp.ProjectVo;
import com.atguigu.scw.vo.resp.AppResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
@RequestMapping("/project")
@RestController
public class ProjectInfoController {

	@Autowired
	OssTemplate ossTemplate;

	@Autowired
	ProjectInfoService projectInfoService;

	@ApiOperation("[+]获取项目信息详情")
	@GetMapping("/details/info/{projectId}")
	public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId) {
		// 业务层对象查到数据
		TProject p = projectInfoService.getProjectInfo(projectId);
		// 数据封装成数据返回
		ProjectDetailVo projectVo = new ProjectDetailVo();

		// 1、查出这个项目的所有图片
		List<TProjectImages> projectImages = projectInfoService.getProjectImages(p.getId());

		// 2.项目支持者统计
		int supporterCount = projectInfoService.getSupporterCount();

//		log.debug("supporterCount======================================================={}", supporterCount);

		projectVo.setSupporterCount(supporterCount);

		// 3.想项目图片
		for (TProjectImages tProjectImages : projectImages) {
			if (tProjectImages.getImgtype() == 0) {
				projectVo.setHeaderImage(tProjectImages.getImgurl());
			} else {
				List<String> detailsImage = projectVo.getDetailsImage();
				detailsImage.add(tProjectImages.getImgurl());
			}
		}

		// 4、项目的所有支持档位；
		List<TReturn> returns = projectInfoService.getProjectReturns(p.getId());
		projectVo.setProjectReturns(returns);

		// 所有需要的属性都对拷到返回VO
		BeanUtils.copyProperties(p, projectVo);
		return AppResponse.ok(projectVo);
	}

	@ApiOperation("[+]获取项目回报列表")
	@GetMapping("/details/returns/{projectId}")
	public AppResponse<List<TReturn>> detailsReturn(@PathVariable("projectId") Integer projectId) {

		List<TReturn> returns = projectInfoService.getProjectReturns(projectId);
		return AppResponse.ok(returns);
	}

	@ApiOperation("[+]获取项目某个回报档位信息")
	@GetMapping("/details/returns/info/{returnId}")
	public AppResponse<TReturn> returnInfo(@PathVariable("returnId") Integer returnId) {
		TReturn tReturn = projectInfoService.getProjectReturnById(returnId);
		return AppResponse.ok(tReturn);
	}

	@ApiOperation("[+]获取系统所有的项目分类")
	@GetMapping("/types")
	public AppResponse<List<TType>> types() {

		List<TType> types = projectInfoService.getProjectTypes();
		return AppResponse.ok(types);
	}

	@ApiOperation("[+]获取系统所有的项目标签")
	@GetMapping("/tags")
	public AppResponse<List<TTag>> tags() {
		List<TTag> tags = projectInfoService.getAllProjectTags();
		return AppResponse.ok(tags);
	}

	@ApiOperation("[+]获取系统所有的项目")
	@GetMapping("/all")
	public AppResponse<List<ProjectVo>> all() {

		// 1、分步查询，先查出所有项目
		// 2、再查询这些项目图片
		List<ProjectVo> prosVo = new ArrayList<>();

		// 1、连接查询，所有的项目left join 图片表，查出所有的图片
		// left join：笛卡尔积 A*B 1000万*6 = 6000万
		// 大表禁止连接查询；
		// 1.项目查出来
		List<TProject> pros = projectInfoService.getAllProjects();

		// 2.项目进行迭代
		for (TProject tProject : pros) {
			// 3.获得项目的主键
			Integer id = tProject.getId();

			// 4.项目主键作为外键查出图片
			List<TProjectImages> images = projectInfoService.getProjectImages(id);
			// 5.每个表查询出来的数据封装成一个个vo
			ProjectVo projectVo = new ProjectVo();
			BeanUtils.copyProperties(tProject, projectVo);

			for (TProjectImages tProjectImages : images) {
				if (tProjectImages.getImgtype() == 0) {
					projectVo.setHeaderImage(tProjectImages.getImgurl());
				}
			}
			// 6.所有vo放到集合里
			prosVo.add(projectVo);

		}
		// 7.返回集合
		return AppResponse.ok(prosVo);
	}

	// 查热门推荐、分类推荐

}
