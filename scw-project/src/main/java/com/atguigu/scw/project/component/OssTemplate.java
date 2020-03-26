package com.atguigu.scw.project.component;

import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Data
@Slf4j
public class OssTemplate {

	// Endpoint以杭州为例，其它Region请按实际情况填写。
	String endpoint;
	// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录
	// https://ram.console.aliyun.com 创建。
	String accessKeyId;
	String accessKeySecret;
	String bucket;

	public String upload(String filename, InputStream inputStream) {
		try {

			// 创建OSSClient实例。
			OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

			// 上传文件流。
			// InputStream inputStream = new FileInputStream("F:/temp/p1.jpg");
			ossClient.putObject(bucket, "pic/" + filename, inputStream);

			// 关闭OSSClient。
			ossClient.shutdown();

			String filePath = "https://" + bucket + "." + endpoint + "/pic/" + filename;
			log.debug("文件上传成功-{}", filePath);
			// 返回文件名
			return filePath;
		} catch (Exception e) {
			//
			e.printStackTrace();
			log.debug("文件上传失败-{}", filename);
			return null;
		}
	}
}
