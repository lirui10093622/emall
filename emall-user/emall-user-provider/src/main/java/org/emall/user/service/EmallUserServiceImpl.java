package org.emall.user.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.emall.common.enums.ApiResult;
import org.emall.common.exception.InvalidParameterException;
import org.emall.common.model.user.*;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.LoginDto;
import org.emall.user.api.dto.RegisterDto;
import org.emall.user.api.dto.RolesAndPermissionsDto;
import org.emall.user.api.dto.UserInfoDto;
import org.emall.user.api.enums.AccountType;
import org.emall.user.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
    public EmallResponse<UserInfoDto> register(EmallRequest<RegisterDto> request) {
        RegisterDto registerDto = request.getData();
        User user = new User();
        user.setName(registerDto.getName());
        user.setPassword(DigestUtil.md5Hex(registerDto.getPassword()));
        user.setPhone(registerDto.getPhone());
        user.setEmail(registerDto.getEmail());
        userMapper.insert(user);
        return EmallResponse.success(UserInfoDto.from(user));
    }

    @Override
    public EmallResponse<UserInfoDto> login(EmallRequest<LoginDto> request) throws InvalidParameterException {
        LoginDto loginDto = request.getData();
        // 根据登录类型加载用户
        User user = null;
        switch (Optional.ofNullable(loginDto.getAccountType()).orElse(AccountType.AUTO)) {
            case NAME:
                user = userMapper.selectByName(loginDto.getAccount()).orElseThrow(() -> new InvalidParameterException("用户名不存在"));
                break;
            case PHONE:
                user = userMapper.selectByPhone(loginDto.getAccount())
                        .orElseThrow(() -> new InvalidParameterException("手机号不存在"));
                break;
            case EMAIL:
                user = userMapper.selectByEmail(loginDto.getAccount())
                        .orElseThrow(() -> new InvalidParameterException("邮箱不存在"));
                break;
            case AUTO:
            default:
                user = userMapper.selectByAccount(loginDto.getAccount())
                        .orElseThrow(() -> new InvalidParameterException("账号不存在"));
        }
        // 验证密码
        if (!user.getPassword().equals(DigestUtil.md5Hex(loginDto.getPassword()))) {
            throw new InvalidParameterException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new InvalidParameterException("账号已被禁用");
        }

        // 更新登录信息
        User updateUser = new User();
        updateUser.setId(user.getId());
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(loginDto.getIp());
        userMapper.updateById(user);

        return EmallResponse.success(UserInfoDto.from(user));
    }

    @Override
    public EmallResponse<User> getUserInfo(EmallRequest<Long> request) throws InvalidParameterException {
        Long userId = request.getData();
        if (userId == null) {
            throw new InvalidParameterException("用户ID不能为空");
        }
        User user = userMapper.selectById(request.getData());
        if (user != null) {
            return EmallResponse.success(user);
        }
        return EmallResponse.fail(ApiResult.FAIL.getCode(), "user not exists");
    }

    @Override
    public EmallResponse<RolesAndPermissionsDto> getRolesAndPermissions(EmallRequest<Long> request) {
        List<Long> roleIds = userRoleMapper.selectList(new QueryWrapper<UserRole>().lambda()
                        .eq(UserRole::getUserId, request.getData()))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = selectByIds(roleIds, roleMapper::selectByIds);

        List<Long> permissionIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            permissionIds = rolePermissionMapper.selectList(new QueryWrapper<RolePermission>().lambda().in(RolePermission::getRoleId, roleIds))
                    .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        }
        List<Permission> permissions = selectByIds(permissionIds, permissionMapper::selectByIds);

        RolesAndPermissionsDto dto = new RolesAndPermissionsDto();
        dto.setRoles(roles);
        dto.setPermissions(permissions);
        return EmallResponse.success(dto);
    }

    public static <T> List<T> selectByIds(List<? extends Serializable> ids, Function<List<? extends Serializable>, List<T>> selector) {
        if (CollectionUtils.isNotEmpty(ids)) {
            return selector.apply(ids);
        }
        return Lists.newArrayList();
    }
}