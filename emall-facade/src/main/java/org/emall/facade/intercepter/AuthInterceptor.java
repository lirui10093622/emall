package org.emall.facade.intercepter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.emall.common.dto.LoginUser;
import org.emall.facade.annotation.Auth;
import org.emall.user.api.EmallUserService;
import org.emall.user.api.dto.RolesAndPermissionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private EmallUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginUser loginUser = authenticate(request);
        return authorize(request, response, handler, loginUser);
    }

    protected LoginUser authenticate(HttpServletRequest request) {
        LoginUser loginUser = parseLoginUser(request);
        if (!loginUser.isAnonymous()) {
            RolesAndPermissionsDto rolesAndPermissions = userService.getRolesAndPermissions(loginUser.getUserId());
            loginUser.setRoles(rolesAndPermissions.getRoles());
            loginUser.setPermissions(rolesAndPermissions.getPermissions());
        }
        return loginUser;
    }

    private LoginUser parseLoginUser(HttpServletRequest request) {
        Jwts.parser().setSigningKey(jwtConfig.secret).parse(request.getHeader(""), LoginUser.class);
        LoginUser loginUser = new LoginUser();
        loginUser.setAnonymous(true);
        return loginUser;
    }

    protected boolean authorize(HttpServletRequest request, HttpServletResponse response, Object handler, LoginUser loginUser) {
        if (!needLogin(request, response, handler)) {
            return true;
        }
        if (checkPermission(request, response, handler, loginUser)) {
            bindLoginUser(request, response, loginUser);
        }
        return true;
    }

    private boolean checkPermission(HttpServletRequest request, HttpServletResponse response, Object handler, LoginUser loginUser) {
        return true;
    }

    private boolean needLogin(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean needLogin = false;
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