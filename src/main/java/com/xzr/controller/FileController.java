package com.xzr.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xzr.utils.ZipUtils;
import com.xzr.vo.BaseResponse;
import com.xzr.vo.EUTreeNode;
import com.xzr.vo.RestStatus;

@RestController
@RequestMapping("file")
public class FileController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	/**
	 * easyui的tree型目录
	 * 
	 * @return
	 */
	@RequestMapping("initFiles")
	public List<EUTreeNode> initFiles(@RequestParam(value = "id", required = false) String parentId) {
		// TODO windows系统下先用c盘测试 上线时改成linux的
		LOGGER.info("[查询文件] 目录路径:path:{} ", parentId);
		List<EUTreeNode> nodes = new ArrayList<>();
		if (StringUtils.isBlank(parentId)) {
			// 前端没有传具体的路径信息的时候直接获取盘符
			OperatingSystem os = OperatingSystem.getInstance();
			if (os.getVendorName().equals("Linux")) {
				LOGGER.info("[查询文件] 当前是linux操作系统");
				parentId = "/";
			} else {
				LOGGER.info("[查询文件] 当前非linux操作系统 当windows操作系统处理");
				Sigar sigar = new Sigar();
				FileSystem fslist[] = null;
				try {
					fslist = sigar.getFileSystemList();
				} catch (SigarException e) {
					LOGGER.error("查询文件异常了：", e);
					return null;
				}
				for (int i = 0; i < fslist.length; i++) {
					EUTreeNode node = new EUTreeNode();
					node.setId(fslist[i].getDevName());
					node.setState("closed");
					node.setText(fslist[i].getDevName());
					Map<String, String> attributesMap = new HashMap<>();
					attributesMap.put("url", "file.html?path=" + fslist[i].getDevName());
					node.setAttributes(attributesMap);
					nodes.add(node);
				}
				return nodes;
			}

		}
		File file = new File(parentId);
		// 获取所有的子文件/目录
		File[] chidFiles = file.listFiles();
		if (chidFiles != null) {
			for (File childFile : chidFiles) {
				EUTreeNode node = new EUTreeNode();
				Map<String, String> attributesMap = new HashMap<>();
				node.setId(childFile.getAbsolutePath());
				node.setParentId(file.getAbsolutePath());
				node.setText(childFile.getName());
				node.setState(childFile.isDirectory() ? "closed" : "open");
				attributesMap.put("url",
						childFile.isDirectory() ? "file.html?path=" + childFile.getAbsolutePath() : "");
				node.setAttributes(attributesMap);
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * 文件下载
	 * 
	 * @param filePath：文件下载的绝对路径
	 */
	@RequestMapping("downLoad")
	public void downloadFile(HttpServletResponse response, String filePath) {
		if (StringUtils.isNotBlank(filePath)) {
			try {
				filePath = URLDecoder.decode(filePath, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error("url转码异常", e1);
			}
			String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
			// 给文件生成压缩包
			String zipFileName = filePath + ".zip";
			ZipUtils.createZip(filePath, zipFileName, false);
			// 设置文件路径
			File file = new File(zipFileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName + ".zip");// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					return;
				} catch (Exception e) {
					LOGGER.error("下载异常", e);
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							LOGGER.error("下载关闭流异常", e);
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							LOGGER.error("下载关闭流异常", e);
						}
					}
					ZipUtils.clean(new File(zipFileName));
				}
			}
		}
		return;
	}

	/**
	 * 
	 * @param file
	 *            文件
	 * @param filePath
	 *            文件上传的目录
	 * @return
	 */
	@RequestMapping(value = "/upload")
	public BaseResponse<String> upload(@RequestParam("file") MultipartFile file, String filePath) {
		BaseResponse<String> baseResponse = new BaseResponse<>();
		try {
			if (file.isEmpty() || StringUtils.isBlank(filePath)) {
				baseResponse.setRestStatus(RestStatus.UPLOAD_FILE_NULL);
				baseResponse.setT("文件为空");
				LOGGER.info("上传文件为空或文件地址为空file:{},filePath:{}", file, filePath);
				return baseResponse;
			}
			// 获取文件名
			String fileName = file.getOriginalFilename();
			LOGGER.info("上传的文件名为：{}", fileName);
			// 获取文件的后缀名
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			LOGGER.info("文件的后缀名为：{}", suffixName);
			String path = filePath + "\\" + fileName;
			OperatingSystem os = OperatingSystem.getInstance();
			if (os.getVendorName().equals("Linux")) {
				// 如果是linux系统的话 中间的分隔符是"/"
				path = filePath + "/" + fileName;
			}
			LOGGER.info("文件全路径：{}", path);
			File dest = new File(path);
			// 检测是否存在目录
			if (!dest.getParentFile().exists()) {
				// 新建文件夹
				dest.getParentFile().mkdirs();
			}
			// 文件写入
			file.transferTo(dest);
			baseResponse.setT("上传成功");
			return baseResponse;
		} catch (IllegalStateException | IOException e) {
			LOGGER.error("文件上传失败:", e);
			baseResponse.setT("上传失败");
		}
		return baseResponse;
	}

	/**
	 * 文件删除
	 * 
	 * @param filePath
	 * @return
	 */
	@RequestMapping("deleteFile")
	public BaseResponse<String> deleteFile(String filePath) {
		BaseResponse<String> baseResponse = new BaseResponse<>();
		LOGGER.info("[文件删除] filepath:{}", filePath);
		if (StringUtils.isBlank(filePath)) {
			LOGGER.info("[文件删除] filepath:{},删除的文件为空", filePath);
			baseResponse.setRestStatus(RestStatus.PARAM_NULL);
			baseResponse.setT("选择的文件/文件夹为空");
			return baseResponse;
		} else {
			ZipUtils.clean(new File(filePath));
			baseResponse.setRestStatus(RestStatus.SUCCESS);
			baseResponse.setT("删除成功");
			return baseResponse;
		}
	}
}
