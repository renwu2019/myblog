package com.wyy.myblog.dao;

import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.util.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 12:20 星期四
 */
public interface BlogCategoryMapper {

    int getTotalBlogCategories(PageQuery pageQuery);

    List<BlogCategory> getBlogCategoryList(PageQuery pageQuery);

    BlogCategory selectByPrimaryKey(Integer categoryId);

    List<BlogCategory> selectByPrimaryKeys(Integer[] categoryIds);

    BlogCategory selectByCategoryName(String categoryName);

    int insert(BlogCategory blogCategory);

    int insertSelective(BlogCategory blogCategory);

    int updateByPrimaryKeySelective(BlogCategory blogCategory);

    int batchDeleteCategories(Integer[] ids);
}
