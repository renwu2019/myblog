package com.wyy.myblog.service;

import com.wyy.myblog.entity.BlogComment;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;

/**
 * created by 伍猷煜 on 2022/6/18 14:00 星期六
 */
public interface BlogCommentService {

    int getTotalBlogComments();

    PageResult<BlogComment> getCommentsPages(PageQuery pageQuery);

    Boolean batchAudit(Long[] ids);

    Boolean batchDelete(Long[] ids);

    Boolean reply(Long commentId, String replyBody);

    PageResult<BlogComment> getCommentPageByBlogId(Long blogId, Integer commentPage);

    Boolean addComment(BlogComment blogComment);
}
