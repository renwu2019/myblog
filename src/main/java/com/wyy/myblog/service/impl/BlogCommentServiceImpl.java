package com.wyy.myblog.service.impl;

import com.wyy.myblog.dao.BlogCommentMapper;
import com.wyy.myblog.entity.BlogComment;
import com.wyy.myblog.service.BlogCommentService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/18 14:01 星期六
 */
@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Resource
    private BlogCommentMapper mBlogCommentMapper;

    @Override
    public int getTotalBlogComments() {
        return mBlogCommentMapper.getTotalBlogComments(null);
    }

    @Override
    public PageResult getCommentsPages(PageQuery pageQuery) {
        List<BlogComment> blogComments = mBlogCommentMapper.getBlogCommentsList(pageQuery);
        int totalComments = mBlogCommentMapper.getTotalBlogComments(pageQuery);
        return new PageResult(totalComments, pageQuery.getLimit(), pageQuery.getPage(), blogComments);
    }

    @Override
    public Boolean batchAudit(Integer[] ids) {
        // 这种看似严格，但是是有问题的。比如传入了多个值，但是有的记录无需更新，就会导致返回整数小于入参长度。
        // return mBlogCommentMapper.batchAudit(ids) == ids.length;
        return mBlogCommentMapper.batchAudit(ids) > 0;
    }

    @Override
    public Boolean batchDelete(Integer[] ids) {
        return mBlogCommentMapper.batchDelete(ids) > 0;
    }

    @Override
    public Boolean reply(Long commentId, String replyBody) {
        BlogComment blogComment = mBlogCommentMapper.selectByPrimaryKey(commentId);
        // comment不为空且状态为已审核，才能进行回复
        if (blogComment != null && blogComment.getCommentStatus().intValue() == 1) {
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            return mBlogCommentMapper.updateByPrimaryKeySelective(blogComment) > 0;
        }
        return false;
    }

    @Override
    public PageResult getCommentPageByBlogId(Long blogId, Integer commentPage) {
        if (commentPage < 1) return null;
        Map<String, Object> params = new HashMap<>();
        params.put("blogId", blogId);
        params.put("commentStatus", 1);
        params.put("page", commentPage);
        params.put("limit", 8);
        PageQuery pageQuery = new PageQuery(params);
        List<BlogComment> blogCommentsList = mBlogCommentMapper.getBlogCommentsList(pageQuery);
        if (CollectionUtils.isEmpty(blogCommentsList)) return null;
        int total = mBlogCommentMapper.getTotalBlogComments(pageQuery);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogCommentsList);
    }

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return mBlogCommentMapper.insertSelective(blogComment) > 0;
    }
}
