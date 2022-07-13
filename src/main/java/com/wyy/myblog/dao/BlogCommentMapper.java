package com.wyy.myblog.dao;

import com.wyy.myblog.entity.BlogComment;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/16 12:21 星期四
 */
public interface BlogCommentMapper {

    int getTotalBlogComments(Map<?, ?> map);  // 使用通配符是可行的，见测试testBlogCommentMapper

    List<BlogComment> getBlogCommentsList(PageQuery pageQuery);

    int batchAudit(Long[] ids);

    int batchDelete(Long[] ids);

    BlogComment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(BlogComment blogComment);

    int insert(BlogComment blogComment);

    int insertSelective(BlogComment blogComment);
}
