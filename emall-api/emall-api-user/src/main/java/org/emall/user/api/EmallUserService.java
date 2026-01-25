package org.emall.user.api;

import org.emall.common.api.EmallService;
import org.emall.common.exception.EmallException;
import org.emall.common.exception.InvalidParameterException;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.user.api.dto.*;
import org.emall.user.model.User;

public interface EmallUserService extends EmallService {

    EmallResponse<UserInfoDto> register(EmallRequest<RegisterDto> request) throws EmallException;

    EmallResponse<UserInfoDto> login(EmallRequest<LoginDto> request) throws InvalidParameterException;

    EmallResponse<User> getUserInfo(EmallRequest<Long> request) throws InvalidParameterException;

    EmallResponse<RolesAndPermissionsDto> getRolesAndPermissions(EmallRequest<Long> request) throws EmallException;

    EmallResponse<Void> changePassword(EmallRequest<ChangePasswordDto> request) throws EmallException;
}