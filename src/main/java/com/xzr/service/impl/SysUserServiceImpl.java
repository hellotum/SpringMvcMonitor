package com.xzr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzr.mapper.SysUserMapper;
import com.xzr.service.SysUserService;
import com.xzr.vo.SysUser;

@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;

	@Override
	public boolean login(String username, String password) {
		SysUser user = sysUserMapper.findUserByUsernameAndPwd(username, password);
		if (user != null) {
			return true;
		}
		return false;
	}

}
