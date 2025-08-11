package jwt;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.emall.common.dto.LoginInfo;
import org.emall.facade.config.JWTConfig;
import org.emall.facade.support.JWTSupport;
import org.emall.user.api.dto.UserInfoDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author Li Rui
 * @date 2025-08-11
 */
public class JWTTest {

    @Test
    public void testJWT() throws Exception {
        JWTConfig config = new JWTConfig();
        config.setSecret("test");
        config.setTtl(30000);

        JWTSupport support = new JWTSupport();
        Field field = JWTSupport.class.getDeclaredField("config");
        field.setAccessible(true);
        field.set(support, config);

        JSONObject json = new JSONObject();
        json.put("userId", 1L);
        json.put("userName", "test");
        UserInfoDto userInfoDto = new UserInfoDto();
        String token = support.createToken(userInfoDto, new LoginInfo());

        DecodedJWT jwt = support.verify(token);
        System.out.printf("jwt: " + jwt);
    }
}
