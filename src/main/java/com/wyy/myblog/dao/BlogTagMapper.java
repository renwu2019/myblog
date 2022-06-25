package com.wyy.myblog.dao;

import com.wyy.myblog.entity.BlogTag;
import com.wyy.myblog.entity.TagBlogCount;
import com.wyy.myblog.util.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 12:22 星期四
 */
public interface BlogTagMapper {

    int getTotalBlogTags(PageQuery pageQuery);

    List<BlogTag> getTagList(PageQuery pageQuery);

    List<TagBlogCount> getTagBlogCount();

    BlogTag selectByTagName(String tagName);

    int insert(BlogTag blogTag);

    int insertSelective(BlogTag blogTag);

    int batchInsertSelective(List<BlogTag> blogTags);

    int deleteTags(Integer[] ids);
}
