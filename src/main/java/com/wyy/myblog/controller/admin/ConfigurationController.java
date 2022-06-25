package com.wyy.myblog.controller.admin;

import com.wyy.myblog.service.ConfigurationService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import com.wyy.myblog.util.Result;
import com.wyy.myblog.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/22 19:41 星期三
 * 系统配置管理
 */
@Controller
@RequestMapping("/admin/configurations")
public class ConfigurationController {

    @Resource
    private ConfigurationService mConfigurationService;


    /**
     * 获取配置页面同时获取所有配置项
     * @param request
     * @return
     */
    @GetMapping
    public String configWebPage(HttpServletRequest request) {
        request.setAttribute("path", "configurations");
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "admin/configuration";
    }


    @PostMapping("/website")
    @ResponseBody
    public Result<?> updateWebsite(@RequestParam(value = "websiteName", required = false) String websiteName,
                                   @RequestParam(value = "websiteDescription", required = false) String websiteDescription,
                                   @RequestParam(value = "websiteLogo", required = false) String websiteLogo,
                                   @RequestParam(value = "websiteIcon", required = false) String websiteIcon) {
        Map<String, String> map = new HashMap<>();
        if (websiteName != null) {
            map.put("websiteName", websiteName);
        }
        if (websiteDescription != null) {
            map.put("websiteDescription", websiteDescription);
        }
        if (websiteLogo != null) {
            map.put("websiteLogo", websiteLogo);
        }
        if (websiteIcon != null) {
            map.put("websiteIcon", websiteIcon);
        }
        if (mConfigurationService.updateConfig(map)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("更新失败");
    }

    @PostMapping("/userInfo")
    @ResponseBody
    public Result<?> updateUserInfo(@RequestParam(value = "yourAvatar", required = false) String yourAvatar,
                                    @RequestParam(value = "yourName", required = false) String yourName,
                                    @RequestParam(value = "yourEmail", required = false) String yourEmail) {
        Map<String, String> map = new HashMap<>();
        if (yourAvatar != null) {
            map.put("yourAvatar", yourAvatar);
        }
        if (yourName != null) {
            map.put("yourName", yourName);
        }
        if (yourEmail != null) {
            map.put("yourEmail", yourEmail);
        }
        if (mConfigurationService.updateConfig(map)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("更新失败");
    }

    @PostMapping("/footer")
    @ResponseBody
    public Result<?> updateUserFooter(@RequestParam(value = "footerAbout", required = false) String footerAbout,
                                      @RequestParam(value = "footerICP", required = false) String footerICP,
                                      @RequestParam(value = "footerCopyRight", required = false) String footerCopyRight,
                                      @RequestParam(value = "footerPoweredBy", required = false) String footerPoweredBy,
                                      @RequestParam(value = "footerPoweredByURL", required = false) String footerPoweredByURL) {
        Map<String, String> map = new HashMap<>();
        if (footerAbout != null) {
            map.put("footerAbout", footerAbout);
        }
        if (footerICP != null) {
            map.put("footerICP", footerICP);
        }
        if (footerCopyRight != null) {
            map.put("yourEmail", footerCopyRight);
        }
        if (footerPoweredBy != null) {
            map.put("footerPoweredBy", footerPoweredBy);
        }
        if (footerPoweredByURL != null) {
            map.put("footerPoweredByURL", footerPoweredByURL);
        }
        if (mConfigurationService.updateConfig(map)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("更新失败");
    }

}
