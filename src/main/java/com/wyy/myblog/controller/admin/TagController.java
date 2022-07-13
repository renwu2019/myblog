package com.wyy.myblog.controller.admin;

import com.wyy.myblog.entity.BlogTag;
import com.wyy.myblog.service.BlogTagService;
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
 * created by 伍猷煜 on 2022/6/21 11:39 星期二
 * 标签管理
 */
@Controller
@RequestMapping("/admin/tags")
public class TagController {

    @Resource
    private BlogTagService mBlogTagService;

    /**
     * 获取标签页面
     * @param request
     * @return 标签页面
     */
    @GetMapping
    public String tagWebPage(HttpServletRequest request) {
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    /**
     * 分页获取标签数据
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
        PageResult<BlogTag> data = mBlogTagService.getTagsPages(pageQuery);
        return ResultUtil.success(data);
    }

    /**
     * 新增标签
     * @param tagName
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@RequestParam("tagName") String tagName) {
        if (!StringUtils.hasText(tagName)) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogTagService.saveTag(tagName)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("新增失败");
    }

    /**
     * 删除标签
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result<?> delete(@RequestBody Integer[] ids) {
        if (ids == null || ids.length < 1) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogTagService.batchDeleteTags(ids)) {
            return ResultUtil.success();
        } else return ResultUtil.fail("删除失败");
    }
}
