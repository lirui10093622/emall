package org.emall.facade.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.emall.common.enums.ApiResult;
import org.emall.common.response.EmallResponse;
import org.emall.facade.annotation.Auth;
import org.emall.facade.utils.RandomTextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Auth(needLogin = false)
@Controller
@RequestMapping("api/captcha")
public class CaptchaController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    private String keepText;

    @GetMapping("generate")
    public void generate(HttpServletResponse httpServletResponse) throws IOException {
        String text = RandomTextUtils.generate(6); // defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        ImageIO.write(image, "JPEG", httpServletResponse.getOutputStream());
        keepText = text;
    }

    @GetMapping("validate")
    @ResponseBody
    public EmallResponse<Void> validate(@Param("text") String text) {
        return Objects.equals(keepText, text) ? EmallResponse.success() : EmallResponse.fail(ApiResult.FAIL.getCode(), "text is wrong");
    }
}