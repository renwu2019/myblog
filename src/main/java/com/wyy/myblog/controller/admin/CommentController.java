package com.wyy.myblog.controller.admin;

import com.wyy.myblog.service.BlogCommentService;
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
 * created by 伍猷煜 on 2022/6/18 21:06 星期六
 * 评论管理
 */
@Controller
@RequestMapping("/admin/comments")
public class CommentController {

    @Resource
    private BlogCommentService mBlogCommentService;

    /**
     * 获取评论管理页面
     * @param request
     * @return 评论管理页面
     */
    @GetMapping
    public String commentWebPage(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "admin/comment";
    }

    /**
     * 分页获取评论数据
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<?> list(@RequestParam Map<String, Object> params) {
        // 这里简单判断还是可以的，真实项目是否需要考虑值是否合法？如小于0，不能转为整型等问题。
        if (!params.containsKey("page") || !params.containsKey("limit")) {
            return ResultUtil.fail(404, "参数异常！");
        }
        PageQuery pageQuery = new PageQuery(params);
        PageResult data = mBlogCommentService.getCommentsPages(pageQuery);
        return ResultUtil.success(data);
    }

    /**
     * 批量审核
     * @param ids 评论id
     * @return
     */
    @PostMapping("/batchAudit")
    @ResponseBody
    public Result<?> batchAudit(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultUtil.fail("参数异常！");
        }
        if (mBlogCommentService.batchAudit(ids)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.fail("审核失败！");
        }
    }

    /**
     * 批量删除
     * @param ids 评论id
     * @return
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    public Result<?> batchDelete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultUtil.fail("参数异常");
        }
        if (mBlogCommentService.batchDelete(ids)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.fail("删除失败！");
        }
    }

    /**
     * 评论回复
     * @param commentId 评论id
     * @param replyBody 回复内容
     * @return
     */
    @PostMapping("/reply")
    @ResponseBody
    public Result<?> reply(@RequestParam("commentId") Long commentId,
                           @RequestParam("replyBody") String replyBody) {
        if (commentId == null || commentId < 1 || !StringUtils.hasText(replyBody)) {
            return ResultUtil.fail("参数异常！");
        }
        if (mBlogCommentService.reply(commentId, replyBody)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.fail("回复失败");
        }
    }
}
