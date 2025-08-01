package org.emall.user.api;

import org.emall.user.api.dto.RolesAndPermissionsDto;
import org.emall.user.api.request.LoginRequest;
import org.emall.user.api.request.RegisterRequest;
import org.emall.user.api.response.LoginResponse;
import org.emall.user.api.response.RegisterResponse;

public interface EmallUserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RolesAndPermissionsDto getRolesAndPermissions(Long userId);
}