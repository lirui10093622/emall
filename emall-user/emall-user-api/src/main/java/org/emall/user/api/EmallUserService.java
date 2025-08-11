package org.emall.user.api;

import org.emall.common.exception.EmallException;
import org.emall.common.exception.InvalidParameterException;
import org.emall.common.model.user.User;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.user.api.dto.LoginDto;
import org.emall.user.api.dto.RegisterDto;
import org.emall.user.api.dto.RolesAndPermissionsDto;
import org.emall.user.api.dto.UserInfoDto;

public interface EmallUserService {

    EmallResponse<UserInfoDto> register(EmallRequest<RegisterDto> request) throws EmallException;

    EmallResponse<UserInfoDto> login(EmallRequest<LoginDto> request) throws InvalidParameterException;

    EmallResponse<User> getUserInfo(EmallRequest<Long> request) throws InvalidParameterException;

    EmallResponse<RolesAndPermissionsDto> getRolesAndPermissions(EmallRequest<Long> request) throws EmallException;
}