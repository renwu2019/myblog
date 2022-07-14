package com.wyy.myblog.controller.blog;

import com.wyy.myblog.controller.vo.BlogBasicVO;
import com.wyy.myblog.controller.vo.BlogDetailVO;
import com.wyy.myblog.entity.BlogComment;
import com.wyy.myblog.entity.BlogLink;
import com.wyy.myblog.service.*;
import com.wyy.myblog.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/23 11:24 星期四
 * 博客展示
 */
@Controller
public class MyBlogController {

    private static final Logger log = LoggerFactory.getLogger(MyBlogController.class);

    private static final String mTheme = "amaze";
    //public static final String theme = "default";
    //public static final String theme = "yummy-jekyll";

    @Resource
    private BlogService mBlogService;

    @Resource EsBlogService mEsBlogService;

    @Resource
    private BlogTagService mBlogTagService;

    @Resource
    private ConfigurationService mConfigurationService;

    @Resource
    private BlogLinkService mBlogLinkService;

    @Resource
    private BlogCommentService mBlogCommentService;

    /**
     * 博客首页
     * @param request
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") Integer pageNum) {
        PageResult<BlogBasicVO> pageResult = mBlogService.getBlogBasicVOPage(pageNum);
        if (pageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("newBlogs", mBlogService.getBlogSimpleVOSortedBy(1));
        request.setAttribute("hotBlogs", mBlogService.getBlogSimpleVOSortedBy(0));
        request.setAttribute("hotTags", mBlogTagService.getTagBlogCounts());
        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "blog/" + mTheme + "/index";
    }

    /**
     * 获取博客详情页
     */
    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request,
                         @PathVariable("blogId") Long blogId,
                         // 评论页请求参数
                         @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = mBlogService.getBlogDetailVOById(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", mBlogCommentService.getCommentPageByBlogId(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "blog/" + mTheme + "/detail";
    }

    /**
     * 测试当前环境下，最简单的接口并发处理能力。
     * @param request
     * @return
     */
    @GetMapping("/blog/test")
    @ResponseBody
    public Result<?> test(HttpServletRequest request) {
        return ResultUtil.success();
    }

    /**
     * 获取友链页面
     */
    @GetMapping("/link")
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");

        Map<Byte, List<BlogLink>> links = mBlogLinkService.getLinkGroups();
        if (links != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (links.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", links.get((byte) 0));
            }
            if (links.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", links.get((byte) 1));
            }
            if (links.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", links.get((byte) 2));
            }
        }
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "blog/" + mTheme + "/link";
    }


    /**
     * 获取关于页面 实际上就是一个博客详情页，这个博客可以通过subUrl字段查询获得，
     * 也就是说若博客有subUrl字段，那么就可以直接通过域名+subUrl的方式访问
     * 这个页面没有评论页
     */
    @GetMapping("/{subUrl}")
    public String subUrlPage(HttpServletRequest request, @PathVariable("subUrl") String subUrl) {

        BlogDetailVO blogDetailVO = mBlogService.getBlogDetailVOBySubUrl(subUrl);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations", mConfigurationService.getAllConfigs());
            return "blog/" + mTheme + "/detail";
        } else {
            return "error/error_400";
        }
    }

    /**
     * 获取分类页面 返回第1页分类页面
     */
    @GetMapping("/category/{categoryName}")
    public String categoryPage(HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        return categoryPage(request, categoryName, 1);
    }

    @GetMapping("/category/{categoryName}/{pageNum}")
    public String categoryPage(HttpServletRequest request,
                               @PathVariable("categoryName") String categoryName,
                               @PathVariable("pageNum") Integer pageNum) {
        PageResult<BlogBasicVO> pageResult = mBlogService.getBlogBasicVOByCategory(categoryName, pageNum);
        request.setAttribute("pageUrl", "category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("newBlogs", mBlogService.getBlogSimpleVOSortedBy(1));
        request.setAttribute("hotBlogs", mBlogService.getBlogSimpleVOSortedBy(0));
        request.setAttribute("hotTags", mBlogTagService.getTagBlogCounts());
        request.setAttribute("pageName", "分类");
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "blog/" + mTheme + "/list";
    }


    /**
     * 获取标签页面
     */
    @GetMapping("/tag/{tagName}")
    public String tagPage(HttpServletRequest request, @PathVariable("tagName") String tagName) {
        return tagPage(request, tagName, 1);
    }

    @GetMapping("/tag/{tagName}/{pageNum}")
    public String tagPage(HttpServletRequest request,
                          @PathVariable("tagName") String tagName,
                          @PathVariable("pageNum") Integer pageNum) {
        PageResult<BlogBasicVO> pageResult = mBlogService.getBlogBasicVOByTag(tagName, pageNum);
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("newBlogs", mBlogService.getBlogSimpleVOSortedBy(1));
        request.setAttribute("hotBlogs", mBlogService.getBlogSimpleVOSortedBy(0));
        request.setAttribute("hotTags", mBlogTagService.getTagBlogCounts());
        request.setAttribute("pageName", "标签");
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "blog/" + mTheme + "/list";
    }

    /**
     * 搜索文章
     */
    @GetMapping("/search/{keyword}")
    public String searchPage(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return searchPage(request, keyword, 1);
    }

    @GetMapping("/search/{keyword}/{pageNum}")
    public String searchPage(HttpServletRequest request,
                             @PathVariable("keyword") String keyword,
                             @PathVariable("pageNum") Integer pageNum) {
        // 数据库中查找
        // PageResult<BlogBasicVO> pageResult = mBlogService.getBlogBasicVOByKeyword(keyword, pageNum);
        // ES中查找
        log.debug("搜索请求：keyword = {}, pageNum = {}", keyword, pageNum); // 用户理解的页号从1开始，程序理解的是从0开始
        PageResult<BlogBasicVO> pageResult = mEsBlogService.search(keyword, pageNum, 9);
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        request.setAttribute("blogPageResult", pageResult);
        request.setAttribute("newBlogs", mBlogService.getBlogSimpleVOSortedBy(1));
        request.setAttribute("hotBlogs", mBlogService.getBlogSimpleVOSortedBy(0));
        request.setAttribute("hotTags", mBlogTagService.getTagBlogCounts());
        request.setAttribute("pageName", "搜索");
        request.setAttribute("configurations", mConfigurationService.getAllConfigs());
        return "blog/" + mTheme + "/list";
    }


    /**
     * 添加评论
     */

    @PostMapping("/blog/comment")
    @ResponseBody
    public Result<?> comment(HttpServletRequest request, HttpSession session,
                             @RequestParam Long blogId, @RequestParam String verifyCode,
                             @RequestParam String commentator, @RequestParam String email,
                             @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (!StringUtils.hasText(verifyCode)) {
            return ResultUtil.fail("验证码不能为空");
        }
        // 正常情况这个字段在评论页是存在的，如果没有，可能通过非法操作请求该接口。
        String kaptchaCode = session.getAttribute("verifyCode").toString();
        if (!StringUtils.hasText(kaptchaCode) ||
                blogId == null || blogId < 1 ||
                // Referer https://blog.csdn.net/u012206617/article/details/123477445
                !StringUtils.hasText(request.getHeader("Referer"))) {
            return ResultUtil.fail("非法操作");
        }
        if (!kaptchaCode.equals(verifyCode.toLowerCase())) {
            return ResultUtil.fail("验证码错误");
        }
        if (!StringUtils.hasText(commentator)) {
            return ResultUtil.fail("请输入您的名称");
        }
        if (!StringUtils.hasText(email) || !PatternUtil.isEmail(email)) {
            return ResultUtil.fail("请输入正确的邮箱地址");
        }
        if (!StringUtils.hasText(commentBody)) {
            return ResultUtil.fail("请输入评论内容");
        }
        if (commentBody.length() > 200) {
            return ResultUtil.fail("评论内容过长");
        }
        BlogComment blogComment = new BlogComment();
        blogComment.setBlogId(blogId);
        blogComment.setCommentator(MyBlogUtils.cleanString(commentator));  // 字符变换
        blogComment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            blogComment.setWebsiteUrl(websiteUrl);
        }
        blogComment.setCommentBody(MyBlogUtils.cleanString(commentBody));  // 字符变换
        if (mBlogCommentService.addComment(blogComment)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.fail("添加评论失败");
        }

    }

}
