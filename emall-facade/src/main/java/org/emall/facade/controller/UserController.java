package org.emall.facade.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.enums.ApiResultCode;
import org.emall.facade.vo.ApiResponse;
import org.emall.facade.vo.LoginVo;
import org.emall.facade.vo.RegisterVo;
import org.emall.facade.vo.UserInfoVo;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.request.RegisterRequest;
import org.emall.user.api.response.RegisterResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserController {
    @DubboReference(lazy = true, check = false)
    private EmallUserService emallUserService;

    @RequestMapping("register")
    public ApiResponse register(@RequestBody @Valid RegisterVo registerVo) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(registerVo.getUsername());
        request.setPassword(registerVo.getPassword());
        request.setPhone(registerVo.getPhone());
        request.setEmail(registerVo.getEmail());
        RegisterResponse response = emallUserService.register(request);
        if (response.isSuccess()) {
            return ApiResponse.success(UserInfoVo.from(response.getUserInfo()));
        }
        return ApiResponse.fail(ApiResultCode.FAIL.getCode(), response.getMessage());
    }

    @RequestMapping("login")
    public ApiResponse login(@RequestBody @Valid LoginVo registerVo) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(registerVo.getUsername());
        request.setPassword(registerVo.getPassword());
        request.setPhone(registerVo.getPhone());
        request.setEmail(registerVo.getEmail());
        RegisterResponse response = emallUserService.register(request);
        if (response.isSuccess()) {
            return ApiResponse.success(UserInfoVo.from(response.getUserInfo()));
        }
        return ApiResponse.fail(ApiResultCode.FAIL.getCode(), response.getMessage());
    }
}