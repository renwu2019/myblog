package com.wyy.myblog.service.impl;

import com.wyy.myblog.dao.BlogTagMapper;
import com.wyy.myblog.dao.BlogTagRelationMapper;
import com.wyy.myblog.entity.BlogTag;
import com.wyy.myblog.entity.BlogTagRelation;
import com.wyy.myblog.entity.TagBlogCount;
import com.wyy.myblog.service.BlogTagService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/18 14:39 星期六
 */
@Service
public class BlogTagServiceImpl implements BlogTagService {

    @Resource
    private BlogTagMapper mBlogTagMapper;

    @Resource
    private BlogTagRelationMapper mBlogTagRelationMapper;


    @Override
    public int getTotalBlogTags() {
        return mBlogTagMapper.getTotalBlogTags(null);
    }

    @Override
    public PageResult<BlogTag> getTagsPages(PageQuery pageQuery) {
        List<BlogTag> tagList = mBlogTagMapper.getTagList(pageQuery);
        int total = mBlogTagMapper.getTotalBlogTags(pageQuery);
        return new PageResult<>(total, pageQuery.getLimit(), pageQuery.getPage(), tagList);
    }

    @Override
    public List<TagBlogCount> getTagBlogCounts() {
        return mBlogTagMapper.getTagBlogCount();
    }

    @Override
    public Boolean saveTag(String tagName) {
        BlogTag blogTag = mBlogTagMapper.selectByTagName(tagName);
        if (blogTag == null) {
            blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            return mBlogTagMapper.insertSelective(blogTag) > 0;
        } return false;  // 标签重复
    }

    @Override
    public Boolean batchDeleteTags(Integer[] ids) {
        // 博客标签关联表中如果有标签的话，禁止删除标签数据
        List<Long> longs = mBlogTagRelationMapper.selectDistinctTagIds(ids);
        if (!CollectionUtils.isEmpty(longs)) {
            return false;
        }
        return mBlogTagMapper.deleteTags(ids) > 0;
    }
}
