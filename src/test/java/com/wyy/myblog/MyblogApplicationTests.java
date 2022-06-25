package com.wyy.myblog;

import com.wyy.myblog.dao.*;
import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.service.BlogService;
import com.wyy.myblog.util.PageQuery;
import org.junit.jupiter.api.Test;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class MyblogApplicationTests {

    @Resource
    private BlogMapper mBlogMapper;

    @Resource
    private BlogCommentMapper mBlogCommentMapper;

    @Resource
    private BlogCategoryMapper mBlogCategoryMapper;

    @Resource
    private BlogTagMapper mBlogTagMapper;

    @Resource
    private BlogLinkMapper mBlogLinkMapper;

    @Test
    void contextLoads() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("page", 1);
        map.put("limit", 2);
        map.put("keyword", "hh");
        PageQuery pageQuery = new PageQuery(map);
        System.out.println(mBlogMapper.getTotalBlogs(pageQuery));
    }

    @Test
    public void testBlogMapper() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 2);
        params.put("limit", 8);  // 分页大小为8
        params.put("blogStatus", 1);  // 博客状态必须为发布状态
        PageQuery pageQuery = new PageQuery(params);
        // 如果没有的话是返回一个空列表，而不是null
        List<Blog> blogList = mBlogMapper.getBlogList(pageQuery);
        System.out.println(blogList);
    }

    @Test
    public void testBlogCommentMapper() {
        Map<String, Integer> params = new HashMap<>();
        params.put("blogId", 4);
        // 传字符串也是可行的，mysql会将字符串转为整型再比较，但不提倡:)
        // Map<String, String> params = new HashMap<>();
        // params.put("blogId", "4a");
        System.out.println("评论数量：" + mBlogCommentMapper.getTotalBlogComments(params));

        System.out.println("根据主键查询的评论：" + mBlogCommentMapper.selectByPrimaryKey(29L));
    }

    @Test
    public void testBlogCategoryMapper() {
        System.out.println("类别数：" + mBlogCategoryMapper.getTotalBlogCategories(null));

        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryId(123);
        blogCategory.setCategoryName("好好");
        blogCategory.setCategoryIcon("/admin/dd");
        blogCategory.setCategoryRank(34);
        blogCategory.setIsDeleted((byte) 0);
        blogCategory.setCreateTime(new Date());
        mBlogCategoryMapper.insert(blogCategory);
    }

    @Test
    public void testBlogTagMapper() {
        System.out.println("标签数：" + mBlogTagMapper.getTotalBlogTags(null));
    }

    @Test
    public void testBlogLinkMapper() {
        System.out.println("友链数：" + mBlogLinkMapper.getTotalBlogLinks(null));
    }
}
