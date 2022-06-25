package com.wyy.myblog.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * created by 伍猷煜 on 2022/6/15 14:50 星期三
 * 公共控制器
 */
@RequestMapping("/common")
@Controller
public class CommonController {

    @Resource
    private DefaultKaptcha mDefaultKaptcha;

    /**
     * 生产验证码
     * @param request
     * @param response
     */
    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = mDefaultKaptcha.createText();
            request.getSession().setAttribute("verifyCode", verifyCode.toLowerCase());
            BufferedImage challenge = mDefaultKaptcha.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 直接将生成的验证码图片字节流写到响应体的输出流中
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
