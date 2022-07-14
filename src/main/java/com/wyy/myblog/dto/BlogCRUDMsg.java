package com.wyy.myblog.dto;

import java.io.Serializable;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/7/14 21:18 星期四
 * 博客变动消息
 */
public class BlogCRUDMsg implements Serializable {

    private List<Long> blogIds;

    private int type;

    public List<Long> getBlogIds() {
        return blogIds;
    }

    public void setBlogIds(List<Long> blogIds) {
        this.blogIds = blogIds;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BlogVaryMsg{" +
                "blogIds=" + blogIds +
                ", type=" + type +
                '}';
    }
}
