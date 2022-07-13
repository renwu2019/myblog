package com.wyy.myblog.service;

import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;

import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/18 14:32 星期六
 */
public interface BlogCategoryService {

    int getTotalBlogCategories();

    PageResult<BlogCategory> getCategoriesPages(PageQuery pageQuery);

    List<BlogCategory> getAllCategories();

    Boolean saveCategory(String categoryName, String categoryIcon);

    Boolean editCategory(Integer categoryId, String categoryName, String categoryIcon);

    Boolean batchDeleteCategories(Integer[] ids);
}
