package org.emall.user.api;

import org.emall.common.exception.EmallException;
import org.emall.user.api.dto.LoginRequest;
import org.emall.user.api.dto.LoginResponse;
import org.emall.user.api.dto.RegisterRequest;
import org.emall.user.api.dto.RegisterResponse;

public interface EmallUserService {

    LoginResponse login(LoginRequest request) throws EmallException;

    RegisterResponse register(RegisterRequest request) throws EmallException;
}