package com.xzr.task;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xzr.mapper.CpuStatusMapper;
import com.xzr.vo.CpuModel;

@Component
public class CpuinfoTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(CpuinfoTask.class);

	@Autowired
	private CpuStatusMapper cpuStatusMapper;

	@Scheduled(cron = "* * * * * ?")
	public void updateCpuinfo() {
		LOGGER.info("执行每一秒钟更新一次cpu信息的定时任务：");
		OperatingSystem os = OperatingSystem.getInstance();
		int type = os.getVendorName().equals("Linux") ? 2 : 1;
		Sigar sigar = new Sigar();
		CpuPerc[] cpuPerc = null;
		try {
			cpuPerc = sigar.getCpuPercList();
		} catch (SigarException e) {
			LOGGER.error("获取cpu的信息异常:", e);
		}
		List<CpuModel> models = new ArrayList<>();

		String dbCpuinfo = cpuStatusMapper.getCpuinfo(type);
		List<CpuModel> dbCpuModels = JSON.parseArray(dbCpuinfo, CpuModel.class);
		for (int i = 0; i < cpuPerc.length; i++) {
			CpuModel model = (CpuModel) dbCpuModels.get(i);
			CpuModel newModel = new CpuModel();
			newModel.setEight(model.getSeven());
			newModel.setSeven(model.getSix());
			newModel.setSix(model.getFive());
			newModel.setFive(model.getFour());
			newModel.setFour(model.getThree());
			newModel.setThree(model.getTwo());
			newModel.setTwo(model.getOne());
			newModel.setOne(cpuPerc[i].getCombined());
			models.add(newModel);
		}
		cpuStatusMapper.updateCpuInfo(JSON.toJSONString(models), type);
	}
}
