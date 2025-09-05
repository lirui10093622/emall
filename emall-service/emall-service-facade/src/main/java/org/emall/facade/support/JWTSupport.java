package org.emall.facade.support;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.emall.user.api.dto.LoginInfo;
import org.emall.user.api.dto.LoginUser;
import org.emall.facade.config.JWTConfig;
import org.emall.user.api.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author Li Rui
 * @date 2025-08-07
 */
@Slf4j
@Component
public class JWTSupport {
    @Autowired
    private JWTConfig config;

    public String createToken(UserInfoDto userInfoDto, LoginInfo loginInfo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, config.getTtl());

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("userId", userInfoDto.getId());
        builder.withClaim("userName", userInfoDto.getName());
        builder.withClaim("loginInfo", JSON.toJSONString(loginInfo));
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(config.getSecret()));
        log.info("createToken, token: {}", token);
        return token;
    }

    public DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(config.getSecret())).build().verify(token);
    }

    public LoginUser parseToken(String token) {
        LoginUser loginUser = new LoginUser();
        try {
            DecodedJWT jwt = verify(token);
            Long userId = jwt.getClaims().get("userId").asLong();
            String name = jwt.getClaims().get("userName").asString();
            LoginInfo loginInfo = JSON.parseObject(jwt.getClaims().get("loginInfo").asString(), LoginInfo.class);
            loginUser.setId(userId);
            loginUser.setName(name);
            loginUser.setLoginInfo(loginInfo);
            loginUser.setAuthenticated(true);
            log.info("parseToken, loginUser: {}", loginUser);
        } catch (JWTVerificationException e) {
            log.warn("jwt verify failed", e);
        } catch (Exception e) {
            log.warn("parseToken error", e);
        }
        return loginUser;
    }
}
