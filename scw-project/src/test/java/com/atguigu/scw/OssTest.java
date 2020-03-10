package com.atguigu.scw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class OssTest {

	public static void main(String[] args) throws FileNotFoundException {
		// Endpoint以杭州为例，其它Region请按实际情况填写。
		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录
		// https://ram.console.aliyun.com 创建。
		String accessKeyId = "LTAI4FoXwhP2JGe7FV8RNwfs";
		String accessKeySecret = "VSIVLC9essftG2GTGpaPRGCC0szVJF";

		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		// 上传文件流。
		InputStream inputStream = new FileInputStream("F:/temp/p1.jpg");
		ossClient.putObject("scwtesla", "pic/p1.jpg", inputStream);

		// 关闭OSSClient。
		ossClient.shutdown();
	}

}
