package com.wyy.myblog.controller.admin;

import com.wyy.myblog.entity.BlogLink;
import com.wyy.myblog.service.BlogLinkService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import com.wyy.myblog.util.Result;
import com.wyy.myblog.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/21 13:24 星期二
 * 友链管理
 */
@Controller
@RequestMapping("/admin/links")
public class LinkController {

    @Resource
    private BlogLinkService mBlogLinkService;

    /**
     * 获取友链页面
     * @param request
     * @return
     */
    @GetMapping
    public String linkWebPage(HttpServletRequest request) {
        request.setAttribute("path", "links");
        return "admin/link";
    }

    /**
     * 分页获取友链数据
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<?> list(@RequestParam Map<String, Object> params) {
        if (!params.containsKey("page") || !params.containsKey("limit")) {
            return ResultUtil.fail(404, "参数异常！");
        }
        PageQuery pageQuery = new PageQuery(params);
        PageResult data = mBlogLinkService.getLinksPages(pageQuery);
        return ResultUtil.success(data);
    }

    /**
     * 添加友链
     * @param linkType
     * @param linkName
     * @param linkUrl
     * @param linkRank
     * @param linkDescription
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@RequestParam("linkType") Integer linkType,
                          @RequestParam("linkName") String linkName,
                          @RequestParam("linkUrl") String linkUrl,
                          @RequestParam("linkRank") Integer linkRank,
                          @RequestParam("linkDescription") String linkDescription) {
        if (!StringUtils.hasText(linkName) || !StringUtils.hasText(linkUrl) || !StringUtils.hasText(linkDescription) ||
                linkType == null || linkType < 0 || linkRank == null || linkRank < 0) {
            return ResultUtil.fail("参数异常");
        }
        BlogLink blogLink = new BlogLink();
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkName(linkName);
        blogLink.setLinkUrl(linkUrl);
        blogLink.setLinkRank(linkRank);
        blogLink.setLinkDescription(linkDescription);
        if (mBlogLinkService.saveLink(blogLink)){
            return ResultUtil.success();
        } else return ResultUtil.fail("添加失败");
    }

    /**
     * 获取指定友链id的详细信息 这里前端list接口已经保存了的，，这个应该可以不需要
     * @param linkId
     * @return
     */
    @GetMapping("/info/{id}")
    @ResponseBody
    public Result<?> info(@PathVariable("id") Integer linkId) {
        BlogLink blogLink = mBlogLinkService.getLinkById(linkId);
        return ResultUtil.success(blogLink);
    }

    /**
     * 友链修改
     * @param linkType
     * @param linkName
     * @param linkUrl
     * @param linkRank
     * @param linkDescription
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result<?> update(@RequestParam("linkId") Integer linkId,
                            @RequestParam("linkType") Integer linkType,
                            @RequestParam("linkName") String linkName,
                            @RequestParam("linkUrl") String linkUrl,
                            @RequestParam("linkRank") Integer linkRank,
                            @RequestParam("linkDescription") String linkDescription) {
        BlogLink blogLink = mBlogLinkService.getLinkById(linkId);
        if (blogLink == null) {
            return ResultUtil.fail("无数据");
        }
        if (!StringUtils.hasText(linkName) || !StringUtils.hasText(linkUrl) || !StringUtils.hasText(linkDescription) ||
                linkType == null || linkType < 0 || linkRank == null || linkRank < 0) {
            return ResultUtil.fail("参数异常");
        }
        blogLink.setLinkType(linkType.byteValue());
        blogLink.setLinkName(linkName);
        blogLink.setLinkUrl(linkUrl);
        blogLink.setLinkRank(linkRank);
        blogLink.setLinkDescription(linkDescription);
        if (mBlogLinkService.updateLink(blogLink)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("更新失败");
    }

    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestBody Integer[] ids) {
        if (ids == null || ids.length < 1) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogLinkService.batchDeleteLinks(ids)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("删除失败");
    }
}
