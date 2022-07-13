package com.wyy.myblog.service.impl;

import com.wyy.myblog.dao.BlogCategoryMapper;
import com.wyy.myblog.dao.BlogMapper;
import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.service.BlogCategoryService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/18 14:32 星期六
 */
@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Resource
    private BlogCategoryMapper mBlogCategoryMapper;

    @Resource
    private BlogMapper mBlogMapper;

    @Override
    public int getTotalBlogCategories() {
        return mBlogCategoryMapper.getTotalBlogCategories(null);
    }

    @Override
    public PageResult<BlogCategory> getCategoriesPages(PageQuery pageQuery) {
        List<BlogCategory> blogCategoriesList = mBlogCategoryMapper.getBlogCategoryList(pageQuery);
        int total = mBlogCategoryMapper.getTotalBlogCategories(pageQuery);
        return new PageResult<>(total, pageQuery.getLimit(), pageQuery.getPage(), blogCategoriesList);
    }

    @Override
    public List<BlogCategory> getAllCategories() {
        return mBlogCategoryMapper.getBlogCategoryList(null);
    }

    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        BlogCategory blogCategory = mBlogCategoryMapper.selectByCategoryName(categoryName);
        if (blogCategory == null) {
            blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            // 其他字段使用建表时定义的默认值
            return mBlogCategoryMapper.insertSelective(blogCategory) > 0;
        } else return false; // 重复了
    }

    /**
     * 修改分类 同时需要修改博客表中分类数据
     * 使用@Transactional声明式事务
     * @param categoryId
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    @Override
    @Transactional
    public Boolean editCategory(Integer categoryId, String categoryName, String categoryIcon) {
        BlogCategory blogCategory = mBlogCategoryMapper.selectByPrimaryKey(categoryId);
        if (blogCategory != null) {
            // 更新博客中的分类id和名称
            mBlogMapper.updateBlogCategories(categoryId, categoryName, new Integer[]{categoryId});
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return mBlogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        } else return false;  // 无此分类id数据
    }

    /**
     * 删除ids中的分类，对于博客表，则将其置为默认分类
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public Boolean batchDeleteCategories(Integer[] ids) {
        // 原作者项目这里博客表不强制依赖于分类表，也许博客的分类在分类表中不存在
        mBlogMapper.updateBlogCategories(0, "默认分类", ids);
        return mBlogCategoryMapper.batchDeleteCategories(ids) > 0;
    }
}
