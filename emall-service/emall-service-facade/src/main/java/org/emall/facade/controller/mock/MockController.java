package org.emall.facade.controller.mock;

import lombok.extern.slf4j.Slf4j;
import org.emall.common.response.EmallResponse;
import org.emall.facade.annotation.Auth;
import org.emall.facade.vo.LoginVo;
import org.emall.facade.vo.UserInfoVo;
import org.emall.facade.vo.UserInfoWithCredentialsVo;
import org.emall.user.api.dto.LoginUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Li Rui
 * @date 2025-10-26
 */
@Slf4j
@Auth(needLogin = false)
@RestController
@RequestMapping("api/mock")
public class MockController {

    @RequestMapping("login")
    public EmallResponse<UserInfoWithCredentialsVo> login(@RequestBody LoginVo loginVo, LoginUser loginUser) {
        UserInfoWithCredentialsVo userInfoWithCredentialsVo = new UserInfoWithCredentialsVo("Bearer", "token", new UserInfoVo());
        return EmallResponse.success(userInfoWithCredentialsVo);
    }
}