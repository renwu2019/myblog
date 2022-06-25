package com.wyy.myblog.service;

import com.wyy.myblog.controller.vo.BlogDetailVO;
import com.wyy.myblog.controller.vo.BlogSimpleVO;
import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 12:23 星期四
 */
public interface BlogService {

    int getTotalBlogs();

    PageResult getBlogsPages(PageQuery pageQuery);

    Blog getBlogById(Long blogId);

    Boolean batchDeleteBlogs(Integer[] ids);

    String saveBlog(Blog blog);

    String updateBlogById(Blog blog);

    /**
     * 获取指定页博客基本数据
     * @param pageNum
     * @return
     */
    PageResult getBlogBasicVOPage(Integer pageNum);

    PageResult getBlogBasicVOByCategory(String categoryName, Integer pageNum);

    PageResult getBlogBasicVOByTag(String tagName, Integer pageNum);

    PageResult getBlogBasicVOByKeyword(String keyword, Integer pageNum);

    /**
     * 获取按某个条件排序后的博客简单数据
     * @param type 0-点击最多 1-最新发布
     * @return
     */
    List<BlogSimpleVO> getBlogSimpleVOSortedBy(int type);


    /**
     * 根据subUrl获取博客详情
     * @param subUrl
     * @return
     */
    BlogDetailVO getBlogDetailVOBySubUrl(String subUrl);


    BlogDetailVO getBlogDetailVOById(Long blogId);

}
