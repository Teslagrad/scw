package com.atguigu.scw;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ScwUserApplicationTests {

	@Autowired
	DataSource DataSource;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Test
	public void test01() throws SQLException {
		Connection conn = DataSource.getConnection();
		System.out.println(conn);
		conn.close();// 归还到链接池
	}

	@Test
	public void test02() {
		stringRedisTemplate.opsForValue().set("key111",
				"value111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
	}

	@Test
	public void test03() {
		String string = stringRedisTemplate.opsForValue().get("key111");
		System.out.println(string);
	}

}
