package com.wyy.myblog.service;

import com.wyy.myblog.entity.BlogLink;
import com.wyy.myblog.util.PageQuery;
import com.wyy.myblog.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/18 15:07 星期六
 */
public interface BlogLinkService {

    int getTotalBLogLinks();

    PageResult<BlogLink> getLinksPages(PageQuery pageQuery);

    BlogLink getLinkById(Integer linkId);

    Boolean saveLink(BlogLink blogLink);

    Boolean updateLink(BlogLink blogLink);

    Boolean batchDeleteLinks(Integer[] ids);

    Map<Byte, List<BlogLink>> getLinkGroups();
}
