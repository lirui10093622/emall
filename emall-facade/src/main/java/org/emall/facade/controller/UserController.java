package org.emall.facade.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.enums.ApiResultCode;
import org.emall.facade.vo.ApiResponse;
import org.emall.facade.vo.RegisterVo;
import org.emall.facade.vo.UserInfoVo;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.request.RegisterRequest;
import org.emall.user.api.response.RegisterResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    @DubboReference
    private EmallUserService emallUserService;

    @RequestMapping("register")
    public ApiResponse register(@RequestBody RegisterVo registerVo) {
        RegisterRequest request = new RegisterRequest();
        RegisterResponse response = emallUserService.register(request);
        if (response.isSuccess()) {
            return ApiResponse.success(UserInfoVo.from(response.getUserInfoDto()));
        }
        return ApiResponse.fail(ApiResultCode.FAIL.getCode(), response.getMessage());
    }
}