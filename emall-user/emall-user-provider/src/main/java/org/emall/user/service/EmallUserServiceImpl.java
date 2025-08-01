package org.emall.user.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.common.exception.ParameterInvalidException;
import org.emall.common.model.user.*;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.RolesAndPermissionsDto;
import org.emall.user.api.dto.UserInfoDto;
import org.emall.user.api.request.LoginRequest;
import org.emall.user.api.request.RegisterRequest;
import org.emall.user.api.response.LoginResponse;
import org.emall.user.api.response.RegisterResponse;
import org.emall.user.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class EmallUserServiceImpl implements EmallUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(DigestUtil.md5Hex(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        userMapper.insert(user);
        RegisterResponse response = new RegisterResponse();
        response.setSuccess(true);
        response.setMessage("success");
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(user.getId());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setPhone(user.getPhone());
        userInfoDto.setEmail(user.getEmail());
        response.setUserInfo(userInfoDto);
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据登录类型加载用户
        User user = null;
        switch (request.getLoginType()) {
            case USERNAME:
                user = userMapper.selectByUsername(request.getAccount()).orElseThrow(() -> new ParameterInvalidException("用户名不存在"));
                break;
            case PHONE:
                user = userMapper.selectByPhone(request.getAccount())
                        .orElseThrow(() -> new ParameterInvalidException(("手机号不存在")));
                break;
            case EMAIL:
                user = userMapper.selectByEmail(request.getAccount())
                        .orElseThrow(() -> new ParameterInvalidException("邮箱不存在"));
                break;
            case AUTO:
            default:
                user = userMapper.selectByUsernameOrPhoneOrEmail(request.getAccount())
                        .orElseThrow(() -> new ParameterInvalidException(("账号不存在")));
        }
        // 验证密码
        if (!user.getPassword().equals(DigestUtil.bcrypt(request.getPassword()))) {
            throw new ParameterInvalidException(("密码错误"));
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new ParameterInvalidException(("账号已被禁用"));
        }

        // 更新登录信息
        User updateUser = new User();
        updateUser.setId(user.getId());
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(request.getLoginIp());
        userMapper.updateById(user);
        LoginResponse response = new LoginResponse();
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(user.getId());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setPhone(user.getPhone());
        userInfoDto.setEmail(user.getEmail());
        response.setUserInfo(userInfoDto);
        return response;
    }

    @Override
    public RolesAndPermissionsDto getRolesAndPermissions(Long userId) {
        List<Long> roleIds = userRoleMapper.selectList(new QueryWrapper<UserRole>().lambda()
                        .eq(UserRole::getUserId, userId))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectByIds(roleIds);

        List<Long> permissionIds = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().lambda()
                        .in(RolePermission::getRoleId, roleIds))
                .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> permissions = permissionMapper.selectByIds(permissionIds);

        RolesAndPermissionsDto dto = new RolesAndPermissionsDto();
        dto.setRoles(roles);
        dto.setPermissions(permissions);
        return dto;
    }
}