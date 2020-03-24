package com.atguigu.scw.project.vo.resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.atguigu.scw.project.bean.TReturn;

import lombok.Data;

//首页热点项目
@Data
public class ProjectVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 150624956091711857L;

	private Integer id;// 项目id

	private Integer memberid;// 会员id
//	
//	//第二步：收集项目基本信息以及发起人信息（暂时不做）
//	private List<Integer> typeids; // 项目的分类id
//	private List<Integer> tagids; // 项目的标签id
//	
	private String name;// 项目名称
	private String remark;// 项目简介
//	private Integer money;// 筹资金额
//	private Integer day;// 筹资天数

	private String headerImage;// 项目头部图片
//	private List<String> detailsImage = new ArrayList<String>();// 项目详情图片
	// 发起人信息：自我介绍，详细自我介绍，联系电话，客服电话

	// 第三步：收集回报信息
	private List<TReturn> projectReturns = new ArrayList<TReturn>();// 项目回报
}
