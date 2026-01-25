package org.emall.facade.intercepter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.emall.common.enums.ApiResult;
import org.emall.common.request.EmallRequest;
import org.emall.common.response.EmallResponse;
import org.emall.facade.annotation.Auth;
import org.emall.facade.config.JWTConfig;
import org.emall.facade.support.JWTSupport;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.LoginUser;
import org.emall.user.api.dto.RolesAndPermissionsDto;
import org.emall.user.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JWTConfig jwtConfig;
    @Autowired
    private JWTSupport jwtSupport;
    @DubboReference
    private EmallUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginUser loginUser = authenticate(request);
        if (!authorize(request, response, handler, loginUser)) {
            EmallResponse<Void> emallResponse = EmallResponse.fail(ApiResult.NO_PERMISSION);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(JSON.toJSONString(emallResponse));
            response.getWriter().flush();
            return false;
        }
        return true;
    }

    protected LoginUser authenticate(HttpServletRequest request) {
        LoginUser loginUser = parseLoginUser(request);
        if (loginUser.isAuthenticated()) {
            EmallResponse<RolesAndPermissionsDto> response = userService.getRolesAndPermissions(new EmallRequest<>(loginUser.getId()));
            if (response.isSuccess()) {
                RolesAndPermissionsDto rolesAndPermissions = response.getData();
                loginUser.setRoles(rolesAndPermissions.getRoles());
                loginUser.setPermissions(rolesAndPermissions.getPermissions());
            }
        }
        return loginUser;
    }

    private LoginUser parseLoginUser(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(header)) {
            log.info("http header[{}] is missing", HttpHeaders.AUTHORIZATION);
            return new LoginUser();
        }
        String token = StringUtils.removeStart(header, jwtConfig.getAuthorizationType() + " ");
        if (StringUtils.isBlank(token)) {
            log.info("token is missing");
            return new LoginUser();
        }
        return jwtSupport.parseToken(token);
    }

    protected boolean authorize(HttpServletRequest request, HttpServletResponse response, Object handler, LoginUser loginUser) {
        if (!needLogin(request, response, handler)) {
            return true;
        }
        if (!loginUser.isAuthenticated()) {
            return false;
        }
        if (checkPermission(request, response, handler, loginUser)) {
            bindLoginUser(request, response, loginUser);
        }
        return true;
    }

    private boolean checkPermission(HttpServletRequest request, HttpServletResponse response, Object handler, LoginUser loginUser) {
        AntPathMatcher pathMatcher = new AntPathMatcher("/");
        for (Permission permission : loginUser.getPermissions()) {
            if (pathMatcher.match(permission.getPath(), request.getRequestURI()) &&allowOperation(permission.getOperation(), request)) {
                return true;
            }
        }
        return false;
    }

    private boolean allowOperation(String operation, HttpServletRequest request) {
        if (operation.equals("ALL")) {
            return true;
        }
        return operation.contains(request.getMethod());
    }

    private boolean needLogin(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean needLogin = true;
        if (handler instanceof HandlerMethod) {
            // 方法上@Auth注解的优先级高于类上的注解:
            Auth auth = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
            if (auth == null) {
                Class<?> controllerClass = ((HandlerMethod) handler).getBeanType();
                auth = controllerClass.getAnnotation(Auth.class);
            }
            if (auth != null) {
                needLogin = auth.needLogin();
            }
        }
        return needLogin;
    }

    protected void bindLoginUser(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        request.setAttribute("loginUser", loginUser);
    }
}