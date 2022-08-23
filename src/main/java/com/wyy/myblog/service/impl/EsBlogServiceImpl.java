package com.wyy.myblog.service.impl;

import com.wyy.myblog.controller.vo.BlogBasicVO;
import com.wyy.myblog.dao.BlogCategoryMapper;
import com.wyy.myblog.dao.EsBlogMapper;
import com.wyy.myblog.elasticsearch.document.EsBlog;
import com.wyy.myblog.elasticsearch.repository.EsBlogRepository;
import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.service.EsBlogService;
import com.wyy.myblog.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by 伍猷煜 on 2022/7/14 16:19 星期四
 */
@Service
public class EsBlogServiceImpl implements EsBlogService {

    private static final Logger log = LoggerFactory.getLogger(EsBlogServiceImpl.class);

    @Resource
    private BlogCategoryMapper mBlogCategoryMapper;

    @Resource
    private EsBlogMapper mEsBlogMapper;

    @Resource
    private EsBlogRepository mEsBlogRepository;

    @Override
    public int importAll() {
        // 从数据库导出所有博客数据
        List<EsBlog> allEsBlogList = mEsBlogMapper.getAllEsBlogList(null);
        // 保存到ES
        Iterable<EsBlog> esBlogs = mEsBlogRepository.saveAll(allEsBlogList);
        int cnt = 0;
        for (EsBlog ignored : esBlogs) {
            cnt++;
        }
        return cnt;
    }

    @Override
    public void delete(Long id) {
        mEsBlogRepository.deleteById(id);
    }

    @Override
    public EsBlog create(Long id) {
        EsBlog saved = null;
        // 从数据库导出指定博客数据
        List<EsBlog> allEsBlogList = mEsBlogMapper.getAllEsBlogList(id);
        if (allEsBlogList.size() > 0) {
            EsBlog esBlog = allEsBlogList.get(0);
            saved = mEsBlogRepository.save(esBlog);
        }
        return saved;
    }

    @Override
    public void delete(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            mEsBlogRepository.deleteAllById(ids);
        }
    }

    @Override
    public PageResult<BlogBasicVO> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<EsBlog> esBlogPage = mEsBlogRepository.findByBlogTitleOrBlogContentOrBlogCategoryNameOrBlogTagsOrderByCreateTimeDesc
                (keyword, keyword, keyword, keyword, pageable);
        int totalCount = (int) esBlogPage.getTotalElements();
        log.debug("博客总数量：{}, 博客列表：{}", totalCount, esBlogPage.getContent());
        // log.debug("方式2：博客总数量：{}, 博客内容：{}", esBlogPage1.getTotalElements(), esBlogPage1.getContent());
        List<EsBlog> esBlogs = esBlogPage.getContent();
        List<BlogBasicVO> blogBasicVOS = getBlogBasicVOFromEsBlog(esBlogs);
        return new PageResult<>(totalCount, pageSize, pageNum, blogBasicVOS);
    }

    /**
     * 根据EsBlog生成BlogBasicVO
     * @param blogs
     * @return
     */
    private List<BlogBasicVO> getBlogBasicVOFromEsBlog(List<EsBlog> blogs) {
        List<BlogBasicVO> blogBasicVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(blogs)) return blogBasicVOS;
        // 获取分类数据
        Integer[] categoryIds = blogs.stream().map(EsBlog::getBlogCategoryId).distinct().toArray(Integer[]::new);
        List<BlogCategory> blogCategories = mBlogCategoryMapper.selectByPrimaryKeys(categoryIds);
        Map<Integer, String> idIconMap = blogCategories.stream().collect(
                Collectors.toMap(BlogCategory::getCategoryId, BlogCategory::getCategoryIcon, (key1, key2) -> key2));
        // 生成博客基本数据视图
        for (EsBlog blog : blogs) {
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
