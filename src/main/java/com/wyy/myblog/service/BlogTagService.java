package com.wyy.myblog.service;

import com.wyy.myblog.entity.BlogTag;
import com.wyy.myblog.entity.TagBlogCount;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/18 14:39 星期六
 */
public interface BlogTagService {

    int getTotalBlogTags();

    PageResult getTagsPages(PageQuery pageQuery);

    /**
     * 获取标签对应博客数量
     * @return
     */
    List<TagBlogCount> getTagBlogCounts();

    Boolean saveTag(String tagName);

    Boolean batchDeleteTags(Integer[] ids);
}
