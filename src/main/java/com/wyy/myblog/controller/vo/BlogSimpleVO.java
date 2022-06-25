package com.wyy.myblog.controller.vo;

import java.io.Serializable;

/**
 * created by 伍猷煜 on 2022/6/23 12:08 星期四
 */
public class BlogSimpleVO implements Serializable {

    private Long blogId;

    private String blogTitle;

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

    @Override
    public String toString() {
        return "BlogSimpleVO{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                '}';
    }
}
