package com.wyy.myblog.entity;

/**
 * created by 伍猷煜 on 2022/6/23 16:16 星期四
 * 一个标签对应多少个博客
 */
public class TagBlogCount {

    private Integer tagId;

    private String tagName;

    private Integer blogCount;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(Integer blogCount) {
        this.blogCount = blogCount;
    }

    @Override
    public String toString() {
        return "TagBlogCount{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", blogCount=" + blogCount +
                '}';
    }
}
