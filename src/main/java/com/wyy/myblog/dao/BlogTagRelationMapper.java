package com.wyy.myblog.dao;

import com.wyy.myblog.entity.BlogTagRelation;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 12:22 星期四
 */
public interface BlogTagRelationMapper {

    List<Long> selectDistinctTagIds(Integer[] ids);

    int batchInsertSelective(List<BlogTagRelation> blogTagRelations);

    int deleteByBlogId(Long blogId);
}
