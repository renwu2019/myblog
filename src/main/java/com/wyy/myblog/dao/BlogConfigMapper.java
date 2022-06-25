package com.wyy.myblog.dao;

import com.wyy.myblog.entity.BlogConfig;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 12:21 星期四
 */
public interface BlogConfigMapper {

    int insertSelective(BlogConfig blogConfig);

    List<BlogConfig> selectAll();

    BlogConfig selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(BlogConfig config);
}
