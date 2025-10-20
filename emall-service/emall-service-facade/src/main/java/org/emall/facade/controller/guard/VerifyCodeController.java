package org.emall.facade.controller.guard;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.facade.config.SmsConfig;
import org.emall.facade.vo.SendVerifyCode;
import org.emall.sms.api.EmallSmsService;
import org.emall.sms.api.dto.SmsDto;
import org.emall.sms.api.enums.SmsSceneEnum;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.LoginUser;
import org.emall.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Rui
 * @date 2025-09-02
 */
@Slf4j
@RestController
@RequestMapping("api/guard/verifyCode")
public class VerifyCodeController {
    @DubboReference(lazy = true, check = false)
    private EmallUserService emallUserService;
    @DubboReference(lazy = true, check = false)
    private EmallSmsService emallSmsService;
    @Autowired
    private SmsConfig smsConfig;

    @GetMapping("send")
    public EmallResponse<Void> send(LoginUser loginUser, @RequestBody SendVerifyCode sendVerifyCode) {
        EmallRequest<Long> getUserInfoRequest = new EmallRequest<>(loginUser.getId());
        EmallResponse<User> getUserInfoResponse = emallUserService.getUserInfo(getUserInfoRequest);
        User user = getUserInfoResponse.getData();
        SmsSceneEnum scene = SmsSceneEnum.valueOf(sendVerifyCode.getScene());

        SmsDto smsDto = new SmsDto();
        smsDto.setScene(scene);
        smsDto.setPhone(user.getPhone());
        smsDto.setUserId(user.getId());
        smsDto.setTtl(smsConfig.getTtlMap().getOrDefault(scene.name(), 5 * 60L));
        return emallSmsService.send(new EmallRequest<>(smsDto));
    }
}