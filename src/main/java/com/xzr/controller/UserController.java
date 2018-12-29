package com.xzr.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xzr.service.SysUserService;
import com.xzr.vo.BaseResponse;
import com.xzr.vo.RestStatus;
import com.xzr.vo.SysUser;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private SysUserService sysUserServicel;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	/**

	 * @param username 用户名
		* @param password 密码
		* @return BaseResponse 统一返回vo
		*/
@RequestMapping("login")
public BaseResponse<String> login(HttpSession session, String username, String password) {
		LOGGER.info("请求参数:username:{},password:{}", username, password);
		BaseResponse<String> baseResponse = new BaseResponse<>();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
		LOGGER.info("必填请求参数为空,直接返回前端");
		baseResponse.setRestStatus(RestStatus.PARAM_NULL);
		} else {
		if (sysUserServicel.login(username, password)) {
		LOGGER.info("用户名密码匹配成功；登录成功 username:{},password:{}", username, password);
		session.setAttribute("user", new SysUser(username, password));
		} else {
		LOGGER.info("用户名密码匹配失败；登录失败 username:{},password:{}", username, password);
		baseResponse.setRestStatus(RestStatus.PWD_ERROR);
		}
		}
		return baseResponse;
		}
}