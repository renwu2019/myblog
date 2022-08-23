package com.wyy.myblog.elasticsearch.repository;

import com.wyy.myblog.elasticsearch.document.EsBlog;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 博客ES操作类
 * 继承ElasticsearchRepository接口，
 * 这样就拥有了一些基本的Elasticsearch数据操作方法，同时定义了一个衍生查询方法。
 * created by 伍猷煜 on 2022/7/13 21:45 星期三
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, Long> {

    /**
     * 搜索博客
     * @param blogTitle 博客标题
     * @param blogContent 博客内容
     * @param blogCategoryName 博客分类名
     * @param blogTags 博客标签
     * @param pageable 分页参数
     * @return
     */
    Page<EsBlog> findByBlogTitleOrBlogContentOrBlogCategoryNameOrBlogTagsOrderByCreateTimeDesc(
            String blogTitle, String blogContent, String blogCategoryName, String blogTags, Pageable pageable);

}
