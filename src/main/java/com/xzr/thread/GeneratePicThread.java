package com.xzr.thread;

import com.xzr.utils.ScreenUtils;

/**
 * 生成图片的线程
 *
 */
public class GeneratePicThread implements Runnable {

	@Override
	public void run() {
		ScreenUtils.captureScreen("D:\\screen.png");
	}

}
