package com.xzr.utils;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScreenUtils.class);

	/**
	 * 只支持windows上的屏幕快照
	 * 
	 * @param fileName
	 *            生成图片存放的文件地址
	 */
	public static void captureScreen(String fileName) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRectangle = new Rectangle(screenSize);
		Robot robot;
		try {
			robot = new Robot();
			BufferedImage image = robot.createScreenCapture(screenRectangle);
			// robot.mouseMove(12, 14);
			ImageIO.write(image, "png", new File(fileName));
		} catch (AWTException | IOException e) {
			LOGGER.error("生成屏幕快照异常：", e);
		}
	}

	/*public static void main(String[] args) {
		captureScreen("D:\\screen.png");
	}*/
}