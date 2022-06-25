package com.wyy.myblog.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * created by 伍猷煜 on 2022/6/23 12:08 星期四
 */
public class BlogBasicVO implements Serializable {

    private Long blogId;

    private String blogTitle;

    private String blogSubUrl;

    private String blogCoverImage;

    private Integer blogCategoryId;

    private String blogCategoryName;

    private String blogCategoryIcon;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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

    public String getBlogSubUrl() {
        return blogSubUrl;
    }

    public void setBlogSubUrl(String blogSubUrl) {
        this.blogSubUrl = blogSubUrl;
    }

    public String getBlogCoverImage() {
        return blogCoverImage;
    }

    public void setBlogCoverImage(String blogCoverImage) {
        this.blogCoverImage = blogCoverImage;
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

    public String getBlogCategoryIcon() {
        return blogCategoryIcon;
    }

    public void setBlogCategoryIcon(String blogCategoryIcon) {
        this.blogCategoryIcon = blogCategoryIcon;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BlogBasicVO{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogSubUrl='" + blogSubUrl + '\'' +
                ", blogCoverImage='" + blogCoverImage + '\'' +
                ", blogCategoryId=" + blogCategoryId +
                ", blogCategoryName='" + blogCategoryName + '\'' +
                ", blogCategoryIcon='" + blogCategoryIcon + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
