package org.emall.facade.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.user.api.dto.LoginUser;
import org.emall.user.model.User;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.UserInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Rui
 * @date 2025-08-11
 */
@RestController
@RequestMapping("api/user/info")
public class UserInfoController {
    @DubboReference(lazy = true, check = false)
    private EmallUserService emallUserService;

    @GetMapping("getUserInfoById")
    public EmallResponse<UserInfoDto> getUserInfoById(@RequestParam("id") Long userId) {
        EmallResponse<User> response = emallUserService.getUserInfo(new EmallRequest<>(userId));
        if (response.isSuccess()) {
            return EmallResponse.success(UserInfoDto.from(response.getData()));
        }
        return EmallResponse.fail(response.getCode(), response.getMessage());
    }

    @GetMapping("getMyUserInfo")
    public EmallResponse<UserInfoDto> getMyUserInfo(LoginUser loginUser) {
        EmallResponse<User> response = emallUserService.getUserInfo(new EmallRequest<>(loginUser.getId()));
        if (response.isSuccess()) {
            return EmallResponse.success(UserInfoDto.from(response.getData()));
        }
        return EmallResponse.fail(response.getCode(), response.getMessage());
    }
}