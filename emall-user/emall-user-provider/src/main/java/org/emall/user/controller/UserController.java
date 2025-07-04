package org.emall.user.controller;

import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private EmallUserService emallUserService;

    @RequestMapping("register")
    public boolean register() {
        RegisterRequest request = new RegisterRequest();
        request.setOrderId(1L);
        request.setUserId(1L);
        request.setUseDate("2017-01-01");
        request.setContractName("张三");
        request.setContractPhone("12345678901");
        return true;
    }
}