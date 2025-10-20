package org.emall.facade.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.enums.ApiResult;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.facade.annotation.Auth;
import org.emall.facade.config.JWTConfig;
import org.emall.facade.support.JWTSupport;
import org.emall.facade.vo.LoginVo;
import org.emall.facade.vo.RegisterVo;
import org.emall.facade.vo.UserInfoVo;
import org.emall.facade.vo.UserInfoWithCredentialsVo;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.LoginDto;
import org.emall.user.api.dto.LoginInfo;
import org.emall.user.api.dto.RegisterDto;
import org.emall.user.api.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@Auth(needLogin = false)
@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private JWTConfig jwtConfig;
    @Autowired
    private JWTSupport jwtSupport;
    @DubboReference(lazy = true, check = false)
    private EmallUserService emallUserService;

    @PostMapping("register")
    public EmallResponse<UserInfoWithCredentialsVo> register(@RequestBody @Valid RegisterVo registerVo, HttpServletRequest httpServletRequest) {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setName(registerVo.getName());
        registerDto.setPassword(registerVo.getPassword());
        registerDto.setPhone(registerVo.getPhone());
        registerDto.setEmail(registerVo.getEmail());
        EmallResponse<UserInfoDto> registerResponse = emallUserService.register(new EmallRequest<>(registerDto));
        EmallResponse<UserInfoWithCredentialsVo> response = parse(registerResponse, httpServletRequest);
        log.info("register, isSuccess: {}, message: {}", response.isSuccess(), response.getMessage());
        return response;
    }

    @PostMapping("login")
    public EmallResponse<UserInfoWithCredentialsVo> login(@RequestBody @Valid LoginVo loginVo, HttpServletRequest httpServletRequest) {
        LoginDto loginDto = new LoginDto();
        loginDto.setAccountType(loginVo.getAccountType());
        loginDto.setAccount(loginVo.getAccount());
        loginDto.setPassword(loginVo.getPassword());
        loginDto.setDevice(loginVo.getDevice());
        loginDto.setIp(httpServletRequest.getRemoteAddr());
        EmallResponse<UserInfoDto> loginResponse = emallUserService.login(new EmallRequest<>(loginDto));
        EmallResponse<UserInfoWithCredentialsVo> response = parse(loginResponse, httpServletRequest);
        log.info("login, isSuccess: {}, message: {}", response.isSuccess(), response.getMessage());
        return response;
    }

    EmallResponse<UserInfoWithCredentialsVo> parse(EmallResponse<UserInfoDto> response, HttpServletRequest httpServletRequest) {
        if (response.isSuccess()) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setTime(new Date());
            loginInfo.setIp(httpServletRequest.getRemoteAddr());
            String token = jwtSupport.createToken(response.getData(), loginInfo);
            return EmallResponse.success(new UserInfoWithCredentialsVo(jwtConfig.getAuthorizationType(), token, UserInfoVo.from(response.getData())));
        }
        return EmallResponse.fail(ApiResult.FAIL.getCode(), response.getMessage());
    }
}