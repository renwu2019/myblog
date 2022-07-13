package com.wyy.myblog.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.wyy.myblog.component.RedisOperator;
import com.wyy.myblog.dao.BlogCommentMapper;
import com.wyy.myblog.entity.BlogComment;
import com.wyy.myblog.service.BlogCommentService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * created by 伍猷煜 on 2022/6/18 14:01 星期六
 */
@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Resource
    private BlogCommentMapper mBlogCommentMapper;

    @Resource
    private RedisOperator mRedisOperator;

    @Value("${redis.key.prefix.comment}")
    private String REDIS_KEY_PREFIX_COMMENT;

    @Value("${redis.key.expire.comment}")
    private Long REDIS_KEY_EXPIRE_COMMENT;

    @Override
    public int getTotalBlogComments() {
        return mBlogCommentMapper.getTotalBlogComments(null);
    }

    @Override
    public PageResult<BlogComment> getCommentsPages(PageQuery pageQuery) {
        List<BlogComment> blogComments = mBlogCommentMapper.getBlogCommentsList(pageQuery);
        int totalComments = mBlogCommentMapper.getTotalBlogComments(pageQuery);
        return new PageResult<>(totalComments, pageQuery.getLimit(), pageQuery.getPage(), blogComments);
    }

    @Override
    public Boolean batchAudit(Long[] ids) {
        // 这种看似严格，但是是有问题的。比如传入了多个值，但是有的记录无需更新，就会导致返回整数小于入参长度。
        // return mBlogCommentMapper.batchAudit(ids) == ids.length;
        Set<Long> blogIds = getBlogIdByCommentId(ids);
        boolean success = mBlogCommentMapper.batchAudit(ids) > 0;
        if (success) {  // 删除缓存
            blogIds.forEach(this::deleteKeyCommentByBlogId);
        }
        return success;
    }

    @Override
    public Boolean batchDelete(Long[] ids) {
        Set<Long> blogIds = getBlogIdByCommentId(ids);
        boolean success = mBlogCommentMapper.batchDelete(ids) > 0;
        if (success) { // 删除缓存
            blogIds.forEach(this::deleteKeyCommentByBlogId);
        }
        return success;
    }

    @Override
    public Boolean reply(Long commentId, String replyBody) {
        BlogComment blogComment = mBlogCommentMapper.selectByPrimaryKey(commentId);
        // comment不为空且状态为已审核，才能进行回复
        if (blogComment != null && blogComment.getCommentStatus().intValue() == 1) {
            blogComment.setReplyBody(replyBody);
            blogComment.setReplyCreateTime(new Date());
            boolean success = mBlogCommentMapper.updateByPrimaryKeySelective(blogComment) > 0;
            // 删除缓存
            if (success) {
                deleteKeyCommentByBlogId(blogComment.getBlogId());
            }
            return success;
        }
        return false;
    }

    @Override
    public PageResult<BlogComment> getCommentPageByBlogId(Long blogId, Integer commentPage) {
        if (commentPage < 1) return null;
        String key = REDIS_KEY_PREFIX_COMMENT + ":" + blogId;
        // 从缓存中获取评论数据 当对评论进行像增删改之类的操作时，需要删除缓存
        // 增加评论可以不删除缓存，因为这里评论不是立即显示在网页上，后端审核后才能显示，因此评论缓存没必要变动，审核时需要变动
        if (mRedisOperator.hasKey(key)) {
            Object o = mRedisOperator.get(key);
            return JSONUtil.toBean(o.toString(), new TypeReference<PageResult<BlogComment>>() {}, false);
        }

        // 从数据库中查询评论数据
        Map<String, Object> params = new HashMap<>();
        params.put("blogId", blogId);
        params.put("commentStatus", 1);
        params.put("page", commentPage);
        params.put("limit", 8);
        PageQuery pageQuery = new PageQuery(params);
        List<BlogComment> blogCommentsList = mBlogCommentMapper.getBlogCommentsList(pageQuery);
        if (CollectionUtils.isEmpty(blogCommentsList)) return null;
        int total = mBlogCommentMapper.getTotalBlogComments(pageQuery);
        PageResult<BlogComment> pageResult = new PageResult<>(total, pageQuery.getLimit(), pageQuery.getPage(), blogCommentsList);
        // 设置评论缓存
        mRedisOperator.set(key, JSONUtil.toJsonStr(pageResult), REDIS_KEY_EXPIRE_COMMENT);
        return pageResult;
    }

    @Override
    public Boolean addComment(BlogComment blogComment) {
        return mBlogCommentMapper.insertSelective(blogComment) > 0;
    }

    /**
     * 根据评论id获取博客id
     * @param ids
     * @return
     */
    private Set<Long> getBlogIdByCommentId(Long[] ids) {
        Set<Long> blogIds = new HashSet<>();
        for (Long id : ids) {
            BlogComment blogComment = mBlogCommentMapper.selectByPrimaryKey(id);
            if (blogComment != null) {
                Long blogId = blogComment.getBlogId();
                blogIds.add(blogId);
            }
        }
        return blogIds;
    }

    /**
     * 根据博客id删除评论缓存
     * @param blogId
     */
    private void deleteKeyCommentByBlogId(Long blogId) {
        mRedisOperator.del(REDIS_KEY_PREFIX_COMMENT + ":" + blogId);
    }
}
