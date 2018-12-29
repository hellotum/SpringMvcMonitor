package com.xzr.controller;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xzr.mapper.CpuStatusMapper;
import com.xzr.vo.BaseResponse;
import com.xzr.vo.CpuModel;
import com.xzr.vo.DiskModel;
import com.xzr.vo.RestStatus;

/**
 * 处理监控主机信息的controller
 */
@RestController
@RequestMapping("computer")
public class ComputerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);
	@Autowired
	private CpuStatusMapper cpuStatusMapper;

	@RequestMapping("getCpuInfo")
	public BaseResponse<List<CpuModel>> getCpuInfo() {
		OperatingSystem os = OperatingSystem.getInstance();
		int type = os.getVendorName().equals("Linux") ? 2 : 1;
		BaseResponse<List<CpuModel>> baseResponse = new BaseResponse<>();
		String cpuinfo = cpuStatusMapper.getCpuinfo(type);
		List<CpuModel> models = JSON.parseArray(cpuinfo, CpuModel.class);
		/*List<CpuModel> models = new ArrayList<>();
		Sigar sigar = new Sigar();
		CpuPerc[] cpuPerc = null;
		try {
			cpuPerc = sigar.getCpuPercList();
		} catch (SigarException e) {
			LOGGER.error("获取cpu的信息异常:", e);
			baseResponse.setRestStatus(RestStatus.GET_CPUINFO_EXCEPTION);
			return baseResponse;
		}
		for (CpuPerc cpu : cpuPerc) {
			CpuModel model = new CpuModel(cpu.getUser(), cpu.getSys(), cpu.getWait(), cpu.getNice(), cpu.getIdle(),
					cpu.getCombined());
			models.add(model);
		}
		baseResponse.setT(models);*/
		baseResponse.setRestStatus(RestStatus.SUCCESS);
		baseResponse.setT(models);
		return baseResponse;
	}

	@RequestMapping("getMemoryInfo")
	public BaseResponse<List<DiskModel>> getMemoryInfo(Integer type) {
		BaseResponse<List<DiskModel>> baseResponse = new BaseResponse<>();
		List<DiskModel> models = new ArrayList<>();
		Sigar sigar = new Sigar();
		//获取硬盘信息
		if (type == 1) {
			FileSystem fslist[] = null;
			try {
				fslist = sigar.getFileSystemList();
			} catch (SigarException e1) {
				e1.printStackTrace();
				LOGGER.error("获取磁盘信息异常:", e1);
				baseResponse.setRestStatus(RestStatus.GET_DISKINFO_EXCEPTION);
				return baseResponse;
			}
			for (int i = 0; i < fslist.length; i++) {
				DiskModel model = new DiskModel();
				FileSystem fs = fslist[i];
				FileSystemUsage usage;
				try {
					usage = sigar.getFileSystemUsage(fs.getDirName());
				} catch (SigarException e) {
					LOGGER.error("获取磁盘信息异常(有可能是光驱的原因，先跳过):", e);
					break;
				}
				switch (fs.getType()) {
				case 0: // TYPE_UNKNOWN ：未知
					break;
				case 1: // TYPE_NONE
					break;
				case 2: // TYPE_LOCAL_DISK : 本地硬盘
					// 文件系统可用大小
					LOGGER.info(fs.getDevName() + "可用大小:    " + usage.getAvail() / 1024 + "MB");
					// 文件系统已经使用量
					LOGGER.info(fs.getDevName() + "已经使用量:    " + usage.getUsed() / 1024 + "MB");
					model.setAvailableZoo(usage.getAvail() / 1024);
					model.setUnavailableZoo(usage.getUsed() / 1024);
					model.setDiskName(fs.getDevName());
					models.add(model);
					break;
				case 3:// TYPE_NETWORK ：网络
					break;
				case 4:// TYPE_RAM_DISK ：闪存
					break;
				case 5:// TYPE_CDROM ：光驱
					break;
				case 6:// TYPE_SWAP ：页面交换0
					break;
				default:
					break;
				}
			}
			baseResponse.setT(models);
			return baseResponse;
		} else {
			Mem mem = null;
			try {
				mem = sigar.getMem();
			} catch (SigarException e) {
				LOGGER.error("获取内存信息异常:", e);
				return null;
			}
			DiskModel model = new DiskModel();
			model.setAvailableZoo(mem.getFree());
			model.setUnavailableZoo(mem.getActualUsed());
			model.setDiskName("内存使用情况:");
			models.add(model);
			baseResponse.setT(models);
			return baseResponse;
		}
	}
}
