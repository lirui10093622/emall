package org.emall.facade.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.enums.ApiResult;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.sms.api.EmallSmsService;
import org.emall.sms.api.dto.SmsDto;
import org.emall.sms.api.enums.SmsSceneEnum;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.ChangeMyPasswordDto;
import org.emall.user.api.dto.ChangePasswordDto;
import org.emall.user.api.dto.LoginUser;
import org.emall.user.api.dto.UserInfoDto;
import org.emall.user.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Li Rui
 * @date 2025-08-11
 */
@Slf4j
@RestController
@RequestMapping("api/user/profile")
public class UserProfileController {
    @DubboReference(lazy = true, check = false)
    private EmallUserService emallUserService;
    @DubboReference(lazy = true, check = false)
    private EmallSmsService emallSmsService;

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

    @PostMapping("changeMyPassword")
    public EmallResponse<Void> changeMyPassword(LoginUser loginUser, @RequestBody ChangeMyPasswordDto changeMyPasswordDto) {
        EmallResponse<User> getUserInfoResponse = emallUserService.getUserInfo(new EmallRequest<>(loginUser.getId()));
        User user = getUserInfoResponse.getData();
        SmsDto smsDto = new SmsDto();
        smsDto.setScene(SmsSceneEnum.CHANGE_SELF_PASSWORD);
        smsDto.setPhone(user.getPhone());
        smsDto.setUserId(user.getId());
        EmallResponse<String> smsResponse = emallSmsService.get(new EmallRequest<>(smsDto));
        if (smsResponse.isSuccess() || !Objects.equals(smsResponse.getData(), changeMyPasswordDto.getVerifyCode())) {
            return EmallResponse.fail(ApiResult.FAIL);
        }
        return emallUserService.changePassword(new EmallRequest<>(new ChangePasswordDto(loginUser, changeMyPasswordDto)));
    }
}