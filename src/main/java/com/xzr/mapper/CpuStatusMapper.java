package com.xzr.mapper;

import org.apache.ibatis.annotations.Param;

public interface CpuStatusMapper {
	String getCpuinfo(@Param("type") int type);

	int updateCpuInfo(@Param("cpuinfo") String cpuinfo, @Param("type") int type);
}
