package com.xzr.mapper;

import org.apache.ibatis.annotations.Param;

import com.xzr.vo.SysUser;

public interface SysUserMapper {
	SysUser findUserByUsernameAndPwd(@Param("username") String username, @Param("password") String password);
}
