package com.xzr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xzr.thread.GeneratePicThread;

@RestController
@RequestMapping("os")
public class OsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OsController.class);
	// 获取屏幕快照

	/**
	 * @return 快照路径
	 */
	@RequestMapping("getScreen")
	public String getScreen() {
		LOGGER.info("执行获取屏幕快照请求----");
		// 新开启一个线程异步去生成图片
		new Thread(new GeneratePicThread()).start();
		// ScreenUtils.captureScreen("D:\\screen.png");
		return "/images/screen.png";

	}
}
