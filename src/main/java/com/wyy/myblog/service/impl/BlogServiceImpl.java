package com.wyy.myblog.service.impl;

import cn.hutool.json.JSONUtil;
import com.wyy.myblog.controller.vo.BlogBasicVO;
import com.wyy.myblog.controller.vo.BlogDetailVO;
import com.wyy.myblog.controller.vo.BlogSimpleVO;
import com.wyy.myblog.dao.*;
import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.entity.BlogTag;
import com.wyy.myblog.entity.BlogTagRelation;
import com.wyy.myblog.service.BlogService;
import com.wyy.myblog.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by 伍猷煜 on 2022/6/17 21:44 星期五
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper mBlogMapper;

    @Resource
    private BlogCategoryMapper mBlogCategoryMapper;

    @Resource
    private BlogTagMapper mBlogTagMapper;

    @Resource
    private BlogTagRelationMapper mBlogTagRelationMapper;

    @Resource
    private BlogCommentMapper mBlogCommentMapper;

    @Resource
    private RedisUtil mRedisUtil;

    @Value("${redis.key.prefix.pv}")
    private String REDIS_KEY_PREFIX_PV;

    @Value("${redis.key.expire.pv}")
    private Long PV_EXPIRE_SECONDS;

    @Value("${redis.key.prefix.uv}")
    private String REDIS_KEY_PREFIX_UV;

    @Value("${redis.key.expire.uv}")
    private Long UV_EXPIRE_SECONDS;

    @Value("${redis.key.prefix.detail}")
    private String REDIS_KEY_PREFIX_DETAIL;

    @Value("${redis.key.expire.detail}")
    private Long DETAIL_EXPIRE_SECONDS;


    @Override
    public int getTotalBlogs() {
        return mBlogMapper.getTotalBlogs(null);
    }

    @Override
    public PageResult getBlogsPages(PageQuery pageQuery) {
        List<Blog> blogs = mBlogMapper.getBlogList(pageQuery);
        int total = mBlogMapper.getTotalBlogs(pageQuery);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogs);
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return mBlogMapper.selectByPrimaryKey(blogId);
    }

    @Override
    public Boolean batchDeleteBlogs(Integer[] ids) {
        return mBlogMapper.deleteByPrimaryKeys(ids) > 0;
    }

    /**
     * 保存博客 声明为一个事务
     * @param blog
     * @return
     */
    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        // 分类处理
        BlogCategory blogCategory = mBlogCategoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            // 不存在该分类，就设为默认分类
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        } else {
            // 存在该分类，就设置分类名
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            // 该分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        // 标签处理
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        List<String> tagSet = Arrays.stream(tags).distinct().collect(Collectors.toList());
        if (tags.length != tagSet.size()) {
            return "标签不能重复";
        }
        // 保存文章
        if (mBlogMapper.insertSelective(blog) > 0) {  // 保存成功
            // 如有必要，则更新分类表（排序值）
            if (blogCategory != null) {
                mBlogCategoryMapper.updateByPrimaryKeySelective(blogCategory);
            }
            // 如有必要，则更新标签表
            List<BlogTag> newTags = new ArrayList<>();  // 新增标签，需要插入到标签表
            List<BlogTag> allTags = new ArrayList<>();  // 总标签，需要更新到博客标签关系表
            for (String tagName : tags) {
                BlogTag blogTag = mBlogTagMapper.selectByTagName(tagName);
                if (blogTag == null) {
                    blogTag = new BlogTag();
                    blogTag.setTagName(tagName);
                    newTags.add(blogTag);
                } else {
                    // 不能直接添加new的blogTag，因为新的还没有插入到标签表，还没有标签id
                    allTags.add(blogTag);
                }
            }
            if (newTags.size() > 0) {
                mBlogTagMapper.batchInsertSelective(newTags);
            }
            // 更新博客标签关系表
            allTags.addAll(newTags);
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            for (BlogTag allTag : allTags) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(allTag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if (mBlogTagRelationMapper.batchInsertSelective(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "保存失败";
    }

    @Override
    @Transactional
    public String updateBlogById(Blog blog) {
        Blog blogForUpdate = mBlogMapper.selectByPrimaryKey(blog.getBlogId());
        if (blogForUpdate == null) {
            return "数据不存在";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());

        // 分类id和分类名称字段更新以及分类处理
        BlogCategory blogCategory = mBlogCategoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            // 不存在该分类，就设为默认分类
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        } else {
            // 存在该分类，就设置分类名
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            // 该分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        // 标签处理
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        List<String> tagSet = Arrays.stream(tags).distinct().collect(Collectors.toList());
        if (tags.length != tagSet.size()) {
            return "标签不能重复";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        blogForUpdate.setUpdateTime(new Date());

        if (mBlogMapper.updateByPrimaryKeySelective(blogForUpdate) > 0) {
            // 如有必要，则更新分类表（排序值）
            if (blogCategory != null) {
                mBlogCategoryMapper.updateByPrimaryKeySelective(blogCategory);
            }
            // 如有必要，则更新标签表
            List<BlogTag> newTags = new ArrayList<>();  // 新增标签，需要插入到标签表
            List<BlogTag> allTags = new ArrayList<>();  // 总标签，需要更新到博客标签关系表
            for (String tagName : tags) {
                BlogTag blogTag = mBlogTagMapper.selectByTagName(tagName);
                if (blogTag == null) {
                    blogTag = new BlogTag();
                    blogTag.setTagName(tagName);
                    newTags.add(blogTag);
                } else {
                    // 不能直接添加new的blogTag，因为新的还没有插入到标签表，还没有标签id
                    allTags.add(blogTag);
                }
            }
            if (newTags.size() > 0) {
                mBlogTagMapper.batchInsertSelective(newTags);
            }
            // 更新博客标签关系表
            allTags.addAll(newTags);
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            for (BlogTag allTag : allTags) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(allTag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            // 删除原有博客标签关系
            mBlogTagRelationMapper.deleteByBlogId(blog.getBlogId());
            // 插入新的博客标签关系
            if (mBlogTagRelationMapper.batchInsertSelective(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "更新失败";
    }

    @Override
    public PageResult getBlogBasicVOPage(Integer pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum);
        params.put("limit", 8);  // 分页大小为8
        params.put("blogStatus", 1);  // 博客状态必须为发布状态
        PageQuery pageQuery = new PageQuery(params);
        List<Blog> blogs = mBlogMapper.getBlogList(pageQuery);
        if (blogs.size() == 0) return null;
        List<BlogBasicVO> blogBasicVOS = getBlogBasicVOFromBlog(blogs);
        int total = mBlogMapper.getTotalBlogs(pageQuery);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogBasicVOS);
    }

    @Override
    public PageResult getBlogBasicVOByCategory(String categoryName, Integer pageNum) {
        if (!PatternUtil.validKeyword(categoryName) || pageNum < 1) return null;
        // 这里为什么不在博客表根据博客分类名直接查询？因为返回的BlogBasicVO中的blogCategoryIcon字段博客表中没有
        // 因此必须把分类表中的信息查出来。。这样，其实博客表中的分类名冗余字段感觉也是没必要的。。
        // 获取分类信息
        BlogCategory blogCategory = mBlogCategoryMapper.selectByCategoryName(categoryName);
        if (blogCategory == null) {
            if (!"默认分类".equals(categoryName)) return null; // 没有该分类，且不是默认分类，则无数据
            blogCategory = new BlogCategory();
            blogCategory.setCategoryId(0);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum);
        params.put("limit", 9);
        params.put("blogCategoryId", blogCategory.getCategoryId());
        params.put("blogStatus", 1); //过滤发布状态下的数据
        PageQuery pageQuery = new PageQuery(params);
        List<Blog> blogs = mBlogMapper.getBlogList(pageQuery);
        List<BlogBasicVO> blogBasicVOS = getBlogBasicVOFromBlog(blogs);
        int total = mBlogMapper.getTotalBlogs(pageQuery);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogBasicVOS);
    }

    @Override
    public PageResult getBlogBasicVOByTag(String tagName, Integer pageNum) {
        if (!PatternUtil.validKeyword(tagName) || pageNum < 1) return null;
        BlogTag blogTag = mBlogTagMapper.selectByTagName(tagName);
        if (blogTag == null) return null;
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum);
        params.put("limit", 9);
        params.put("tagId", blogTag.getTagId());
        PageQuery pageQuery = new PageQuery(params);
        List<Blog> blogs = mBlogMapper.getBlogsByTagId(pageQuery);
        int total = mBlogMapper.getTotalBlogs(pageQuery);
        List<BlogBasicVO> blogBasicVOS = getBlogBasicVOFromBlog(blogs);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogBasicVOS);
    }

    @Override
    public PageResult getBlogBasicVOByKeyword(String keyword, Integer pageNum) {
        if (!PatternUtil.validKeyword(keyword) || pageNum < 1) return null;
        Map<String, Object> params = new HashMap<>();
        params.put("page", pageNum);
        params.put("limit", 9);
        params.put("keyword", keyword);
        params.put("blogStatus", 1); //过滤发布状态下的数据
        PageQuery pageQuery = new PageQuery(params);
        List<Blog> blogList = mBlogMapper.getBlogList(pageQuery);
        List<BlogBasicVO> blogBasicVOS = getBlogBasicVOFromBlog(blogList);
        int total = mBlogMapper.getTotalBlogs(pageQuery);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogBasicVOS);
    }

    @Override
    public List<BlogSimpleVO> getBlogSimpleVOSortedBy(int type) {
        List<Blog> blogs = mBlogMapper.getBlogListByType(type, 9);
        List<BlogSimpleVO> simpleVOS = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogSimpleVO blogSimpleVO = new BlogSimpleVO();
            BeanUtils.copyProperties(blog, blogSimpleVO);
            simpleVOS.add(blogSimpleVO);
        }
        return simpleVOS;
    }

    @Override
    public BlogDetailVO getBlogDetailVOBySubUrl(String subUrl) {
        Blog blog = mBlogMapper.selectBySubUrl(subUrl);
        return getBlogDetailVOFromBlog(blog);
    }

    @Override
    public BlogDetailVO getBlogDetailVOById(Long blogId) {
        if (blogId == null || blogId < 1) return null;
        String key = String.valueOf(blogId);
        BlogDetailVO blogDetailVO;
        if (mRedisUtil.hHasKey(REDIS_KEY_PREFIX_DETAIL, key)) {
            // 从缓存中获取
            Object o = mRedisUtil.hGet(REDIS_KEY_PREFIX_DETAIL, key);  // 是一个LinkedHashMap
            String jsonStr = JSONUtil.toJsonStr(o);
            blogDetailVO = JSONUtil.toBean(jsonStr, BlogDetailVO.class);
        } else {
            // 从数据库中获取
            Blog blog = mBlogMapper.selectByPrimaryKey(blogId);
            blogDetailVO = getBlogDetailVOFromBlog(blog);
            // 设置博客详情缓存
            mRedisUtil.hSet(REDIS_KEY_PREFIX_DETAIL, key, blogDetailVO, DETAIL_EXPIRE_SECONDS);
        }
        // 缓存中存在PV key，那么就从缓存中获取PV数据
        if (mRedisUtil.hHasKey(REDIS_KEY_PREFIX_PV, key)) {
            // 不能使用(Long)强转
            Long pv = new Long(mRedisUtil.hGet(REDIS_KEY_PREFIX_PV, key).toString());
            blogDetailVO.setBlogViews(pv);
        } else {  // 缓存中不存在PV key，就设置PV key，同时加上过期时间
            mRedisUtil.hSet(REDIS_KEY_PREFIX_PV, key, blogDetailVO.getBlogViews(), PV_EXPIRE_SECONDS);
        }
        mRedisUtil.hIncr(REDIS_KEY_PREFIX_PV, key, 1);  // 浏览量加1

        return blogDetailVO;
        // Blog blog = mBlogMapper.selectByPrimaryKey(blogId);
        // return getBlogDetailVOFromBlog(blog);
    }

    /**
     * 方法抽取
     * 将blog映射成blogDetailVO对象，需要注意blog中的分类id可能在分类表中不存在，但是它合法的（默认分类id）
     * @param blog
     * @return
     */
    private BlogDetailVO getBlogDetailVOFromBlog(Blog blog) {
        // 不存在或者未发布
        if (blog == null || blog.getBlogStatus() != 1) return null;
        // String blogId = String.valueOf(blog.getBlogId());
        // // 缓存中存在PV key，那么就从缓存中获取PV数据
        // if (mRedisUtil.hHasKey(REDIS_KEY_PREFIX_PV, blogId)) {
        //     // 不能使用(Long)强转
        //     Long pv = new Long(mRedisUtil.hGet(REDIS_KEY_PREFIX_PV, blogId).toString());
        //     blog.setBlogViews(pv);
        // } else {  // 缓存中不存在PV key，就设置PV key，同时加上过期时间
        //     mRedisUtil.hSet(REDIS_KEY_PREFIX_PV, blogId, blog.getBlogViews(), PV_EXPIRE_SECONDS);
        // }
        // mRedisUtil.hIncr(REDIS_KEY_PREFIX_PV, blogId, 1);  // 浏览量加1

        //增加浏览量
        // blog.setBlogViews(blog.getBlogViews() + 1);
        // mBlogMapper.updateByPrimaryKey(blog);
        // 生成BlogDetailVO对象
        BlogDetailVO blogDetailVO = new BlogDetailVO();
        BeanUtils.copyProperties(blog, blogDetailVO);
        BlogCategory blogCategory = mBlogCategoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        // 设置分类信息
        if (blogCategory == null) {
            blogDetailVO.setBlogCategoryId(0);
            blogDetailVO.setBlogCategoryName("默认分类");
            blogDetailVO.setBlogCategoryIcon("/admin/dist/img/category/00.png");
        } else {
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
        }
        // 博客内容转换
        blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blog.getBlogContent()));
        String tag = blog.getBlogTags();
        if (tag != null) {
            blogDetailVO.setBlogTags(Arrays.asList(tag.split(",")));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("blogId", blog.getBlogId());
        params.put("commentStatus", 1);  // 过滤审核通过的评论
        int totalBlogComments = mBlogCommentMapper.getTotalBlogComments(params);
        blogDetailVO.setCommentCount(totalBlogComments);
        return blogDetailVO;
    }


    /**
     * 方法抽取
     * 将blog映射成blogBasicVO对象，需要注意blog中的分类id可能在分类表中不存在，但是它合法的（默认分类id）
     * @param blogs
     * @return
     */
    private List<BlogBasicVO> getBlogBasicVOFromBlog(List<Blog> blogs) {
        List<BlogBasicVO> blogBasicVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(blogs)) return blogBasicVOS;
        // 获取分类数据
        Integer[] categoryIds = blogs.stream().map(Blog::getBlogCategoryId).distinct().toArray(Integer[]::new);
        List<BlogCategory> blogCategories = mBlogCategoryMapper.selectByPrimaryKeys(categoryIds);
        Map<Integer, String> idIconMap = blogCategories.stream().collect(
                Collectors.toMap(BlogCategory::getCategoryId, BlogCategory::getCategoryIcon, (key1, key2) -> key2));
        // 生成博客基本数据视图

        for (Blog blog : blogs) {
            BlogBasicVO basicVO = new BlogBasicVO();
            basicVO.setBlogId(blog.getBlogId());
            basicVO.setBlogTitle(blog.getBlogTitle());
            basicVO.setBlogSubUrl(blog.getBlogSubUrl());
            basicVO.setBlogCoverImage(blog.getBlogCoverImage());
            basicVO.setCreateTime(blog.getCreateTime());
            // 博客分类
            if (idIconMap.containsKey(blog.getBlogCategoryId())) {
                basicVO.setBlogCategoryId(blog.getBlogCategoryId());
                basicVO.setBlogCategoryName(blog.getBlogCategoryName());
                basicVO.setBlogCategoryIcon(idIconMap.get(blog.getBlogCategoryId()));
            } else {
                basicVO.setBlogCategoryId(0);
                basicVO.setBlogCategoryName("默认分类");
                basicVO.setBlogCategoryIcon("/admin/dist/img/category/00.png");
            }
            blogBasicVOS.add(basicVO);
        }
        return blogBasicVOS;
    }
}
