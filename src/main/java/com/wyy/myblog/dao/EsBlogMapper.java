package com.wyy.myblog.dao;

import com.wyy.myblog.elasticsearch.document.EsBlog;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/7/14 16:32 星期四
 */
public interface EsBlogMapper {

    List<EsBlog> getAllEsBlogList(Long blogId);
}
