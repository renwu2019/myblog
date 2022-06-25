package com.wyy.myblog.controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/23 12:09 星期四
 */
public class BlogDetailVO implements Serializable {

    private Long blogId;

    private String blogTitle;

    private String blogCoverImage;

    private String blogContent;

    private Integer blogCategoryId;

    private String blogCategoryName;

    private Integer commentCount;  // 评论表数据

    private String blogCategoryIcon;  // 分类表数据

    private List<String> blogTags;  // 标签 列表类型

    private Long blogViews;

    private Byte enableComment;

    private Date createTime;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogCoverImage() {
        return blogCoverImage;
    }

    public void setBlogCoverImage(String blogCoverImage) {
        this.blogCoverImage = blogCoverImage;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public Integer getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(Integer blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public String getBlogCategoryName() {
        return blogCategoryName;
    }

    public void setBlogCategoryName(String blogCategoryName) {
        this.blogCategoryName = blogCategoryName;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getBlogCategoryIcon() {
        return blogCategoryIcon;
    }

    public void setBlogCategoryIcon(String blogCategoryIcon) {
        this.blogCategoryIcon = blogCategoryIcon;
    }

    public List<String> getBlogTags() {
        return blogTags;
    }

    public void setBlogTags(List<String> blogTags) {
        this.blogTags = blogTags;
    }

    public Long getBlogViews() {
        return blogViews;
    }

    public void setBlogViews(Long blogViews) {
        this.blogViews = blogViews;
    }

    public Byte getEnableComment() {
        return enableComment;
    }

    public void setEnableComment(Byte enableComment) {
        this.enableComment = enableComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BlogDetailVO{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogCoverImage='" + blogCoverImage + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", blogCategoryId=" + blogCategoryId +
                ", blogCategoryName='" + blogCategoryName + '\'' +
                ", commentCount=" + commentCount +
                ", blogCategoryIcon='" + blogCategoryIcon + '\'' +
                ", blogTags=" + blogTags +
                ", blogViews=" + blogViews +
                ", enableComment=" + enableComment +
                ", createTime=" + createTime +
                '}';
    }
}
