package com.wyy.myblog.service.impl;

import com.wyy.myblog.dao.BlogLinkMapper;
import com.wyy.myblog.entity.BlogLink;
import com.wyy.myblog.service.BlogLinkService;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by 伍猷煜 on 2022/6/18 15:08 星期六
 */
@Service
public class BlogLinkServiceImpl implements BlogLinkService {

    @Resource
    private BlogLinkMapper mBlogLinkMapper;


    @Override
    public int getTotalBLogLinks() {
        return mBlogLinkMapper.getTotalBlogLinks(null);
    }

    @Override
    public PageResult getLinksPages(PageQuery pageQuery) {
        List<BlogLink> blogLinks = mBlogLinkMapper.getLinkList(pageQuery);
        int total = mBlogLinkMapper.getTotalBlogLinks(pageQuery);
        return new PageResult(total, pageQuery.getLimit(), pageQuery.getPage(), blogLinks);
    }

    @Override
    public BlogLink getLinkById(Integer linkId) {
        return mBlogLinkMapper.selectByPrimaryKey(linkId);
    }

    @Override
    public Boolean saveLink(BlogLink blogLink) {
        return mBlogLinkMapper.insertSelective(blogLink) > 0;
    }

    @Override
    public Boolean updateLink(BlogLink blogLink) {
        return mBlogLinkMapper.updateByPrimaryKeySelective(blogLink) > 0;
    }

    @Override
    public Boolean batchDeleteLinks(Integer[] ids) {
        return mBlogLinkMapper.batchDeleteLinks(ids) > 0;
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinkGroups() {
        List<BlogLink> linkList = mBlogLinkMapper.getLinkList(null);
        return linkList.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
    }
}
