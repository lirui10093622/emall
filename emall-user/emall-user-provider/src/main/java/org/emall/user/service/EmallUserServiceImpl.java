package org.emall.user.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.emall.common.exception.EmallException;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.LoginRequest;
import org.emall.user.api.dto.LoginResponse;
import org.emall.user.api.dto.RegisterRequest;
import org.emall.user.api.dto.RegisterResponse;
import org.emall.user.mapper.UserMapper;
import org.emall.user.support.ConfigHandler;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class EmallUserServiceImpl implements EmallUserService {
    @Autowired
    private UserMapper orderTaskMapper;
    @Autowired
    private UserMapper orderPassengerMapper;
    @Autowired
    private ConfigHandler configHandler;

    @Override
    public LoginResponse login(LoginRequest request) throws EmallException {
        return null;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) throws EmallException {
        return null;
    }
}