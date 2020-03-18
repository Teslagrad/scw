package com.atguigu.scw.project.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.atguigu.scw.project.component.OssTemplate;

@SpringBootConfiguration
public class AppProjectConfig {

	@ConfigurationProperties(prefix = "oss") // 把配置的值注入进去
	@Bean
	public OssTemplate ossTemplate() {
		return new OssTemplate();
	}
}
