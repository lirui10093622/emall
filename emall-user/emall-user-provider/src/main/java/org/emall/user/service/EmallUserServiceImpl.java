package org.emall.user.service;

import cn.hutool.crypto.digest.DigestUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.request.LoginRequest;
import org.emall.user.api.request.RegisterRequest;
import org.emall.user.api.response.LoginResponse;
import org.emall.user.api.response.RegisterResponse;
import org.emall.user.mapper.UserMapper;
import org.emall.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService
public class EmallUserServiceImpl implements EmallUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(DigestUtil.md5Hex(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        userMapper.insertSelective(user);
        RegisterResponse response = new RegisterResponse();
        response.setSuccess(true);
        response.setMessage("success");
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }
}