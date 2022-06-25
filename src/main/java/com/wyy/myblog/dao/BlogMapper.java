package com.wyy.myblog.dao;

import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/16 12:20 星期四
 */
public interface BlogMapper {

    int getTotalBlogs(PageQuery pageQuery);

    List<Blog> getBlogList(PageQuery pageQuery);

    /**
     * 获取按某个条件排序后的博客数据
     * @param type
     * @return
     */
    List<Blog> getBlogListByType(@Param("type") int type, @Param("limit") int limit);

    Blog selectByPrimaryKey(Long blogId);

    Blog selectBySubUrl(String subUrl);

    /**
     * 更新博客分类
     * @param categoryId 目标分类id
     * @param categoryName 目标分类图标
     * @param ids 待更新的分类id
     * @return
     */
    int updateBlogCategories(@Param("categoryId") Integer categoryId,
                             @Param("categoryName") String categoryName,
                             @Param("ids") Integer[] ids);

    int updateByPrimaryKeySelective(Blog blog);

    int updateByPrimaryKey(Blog blog);

    int batchDeleteByPrimaryKeys(Integer[] ids);

    int insert(Blog blog);

    int insertSelective(Blog blog);

    List<Blog> getBlogsByTagId(PageQuery pageQuery);
}
