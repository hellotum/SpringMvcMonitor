package com.xzr.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xzr.Application;
import com.xzr.mapper.CpuStatusMapper;
import com.xzr.vo.CpuModel;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CpuTest {
	@Autowired
	private CpuStatusMapper cpuStatusMapper;

	@Test
	public void test() {

	}

	public static void main(String[] args) {
		List<CpuModel> models = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			models.add(new CpuModel());
		}
		String string = JSON.toJSONString(models);
		Jedis jedis = new Jedis("123.207.37.133", 16380);
		jedis.set("linux:cpu", string);
		String cpuinfo = jedis.get("linux:cpu");
		models = JSON.parseArray(cpuinfo, CpuModel.class);
	}
}
