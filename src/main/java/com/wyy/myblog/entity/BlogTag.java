package com.wyy.myblog.entity;

import java.util.Date;

/**
 * created by 伍猷煜 on 2022/6/16 11:22 星期四
 */
public class BlogTag {

    private Integer tagId;

    private String tagName;

    private Byte isDeleted;

    private Date createTime;

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

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BlogTag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                '}';
    }
}
