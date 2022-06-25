package com.wyy.myblog.dao;

import com.wyy.myblog.entity.BlogLink;
import com.wyy.myblog.util.PageQuery;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 12:21 星期四
 */
public interface BlogLinkMapper {

    int getTotalBlogLinks(PageQuery pageQuery);

    List<BlogLink> getLinkList(PageQuery pageQuery);

    BlogLink selectByPrimaryKey(Integer linkId);

    int insert(BlogLink blogLink);

    int insertSelective(BlogLink blogLink);

    int updateByPrimaryKeySelective(BlogLink blogLink);

    int deleteByPrimaryKey(Integer linkId);

    int batchDeleteLinks(Integer[] ids);
}
