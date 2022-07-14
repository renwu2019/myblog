package com.wyy.myblog.service;

import com.wyy.myblog.controller.vo.BlogBasicVO;
import com.wyy.myblog.elasticsearch.document.EsBlog;
import com.wyy.myblog.util.PageResult;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/7/13 22:31 星期三
 * 博客搜索管理Service
 */
public interface EsBlogService {

    /**
     * 从数据库中导入所有博客到ES
     * @return
     */
    int importAll();

    /**
     * 根据id删除博客
     * @param id
     */
    void delete(Long id);

    /**
     * 导入指定博客到ES
     * @param id
     * @return
     */
    EsBlog create(Long id);

    /**
     * 批量删除博客
     */
    void delete(List<Long> ids);

    /**
     * 根据关键字搜索
     */
    PageResult<BlogBasicVO> search(String keyword, Integer pageNum, Integer pageSize);
}
