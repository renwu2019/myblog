package com.wyy.myblog.controller.admin;

import com.wyy.myblog.entity.AdminUser;
import com.wyy.myblog.service.*;
import com.wyy.myblog.util.Result;
import com.wyy.myblog.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * created by 伍猷煜 on 2022/6/14 21:46 星期二
 * 管理员管理
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService mAdminUserService;

    @Resource
    private BlogService mBlogService;

    @Resource
    private BlogCommentService mBlogCommentService;

    @Resource
    private BlogCategoryService mBlogCategoryService;

    @Resource
    private BlogTagService mBlogTagService;

    @Resource
    private BlogLinkService mBlogLinkService;

    /**
     *
     * @return 登录页面
     */
    @GetMapping("/login")
    public String getLoginWebPage() {
        return "admin/login";
    }

    /**
     * 登录动作 表单提交
     * @param username 用户名
     * @param password 密码
     * @param verifyCode 验证码
     * @param session 会话
     * @return 登录页面 或 管理员首页
     */
    @PostMapping("/login")
    public String login(@RequestParam("userName") String username, @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode, HttpSession session) {
        // StringUtils.isEmpty()被弃用了，用hasText更好些
        if (!StringUtils.hasText(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode").toString();
        if (!StringUtils.hasText(kaptchaCode) || !verifyCode.toLowerCase().equals(kaptchaCode.toLowerCase())) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        AdminUser adminUser = mAdminUserService.login(username, password);
        if (adminUser == null) {
            session.setAttribute("errorMsg", "登录失败");
            return "admin/login";
        } else {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            session.setMaxInactiveInterval(60 * 60 * 2);
            // session.setMaxInactiveInterval(5);  // 测试 过期后request.getSession().getAttribute("loginUser")返回空
            // 重定向到/admin/index，注意这里是要客户端去请求/admin/index， 而不是直接给index页面
            return "redirect:/admin/index";
        }
    }

    /**
     * 获取管理员页面首页
     * @param request 管理员页面请求对象
     * @return 管理员页面首页
     */
    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path","index");  // path用于控制前端样式
        request.setAttribute("blogCount",mBlogService.getTotalBlogs());
        request.setAttribute("commentCount",mBlogCommentService.getTotalBlogComments());
        request.setAttribute("categoryCount",mBlogCategoryService.getTotalBlogCategories());
        request.setAttribute("tagCount", mBlogTagService.getTotalBlogTags());
        request.setAttribute("linkCount",mBlogLinkService.getTotalBLogLinks());
        return "admin/index";
    }

    /**
     * 获取简介页面
     * @param request
     * @return
     */
    @GetMapping("/profile")
    public String getProfileWebPage(HttpServletRequest request) {
        Object attr = request.getSession().getAttribute("loginUserId");
        if (attr == null) {
            return "admin/login";
        }
        Integer loginUserId = (int)attr;
        AdminUser adminUser = mAdminUserService.getUserDetailById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public Result<?> updateName(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                                @RequestParam("nickName") String nickName) {
        if (!StringUtils.hasText(loginUserName) || !StringUtils.hasText(nickName)) {
            return ResultUtil.fail("参数异常");
        }
        Integer loginUserId = (int)request.getSession().getAttribute("loginUserId");
        if (mAdminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return ResultUtil.success();
        } else return ResultUtil.fail();
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public Result<?> updatePassword(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                    @RequestParam("newPassword") String newPassword) {
        if (!StringUtils.hasText(originalPassword) || !StringUtils.hasText(newPassword)) {
            return ResultUtil.fail("参数异常");
        }
        Integer loginUserId = (int)request.getSession().getAttribute("loginUserId");
        if (mAdminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return ResultUtil.success();
        } else return ResultUtil.fail();
    }
    /**
     * 退出登录
     * @param request
     * @return 登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("verifyCode");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }

}
