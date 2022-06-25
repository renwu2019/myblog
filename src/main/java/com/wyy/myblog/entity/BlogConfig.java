package com.wyy.myblog.entity;

import java.util.Date;

/**
 * created by 伍猷煜 on 2022/6/16 11:22 星期四
 */
public class BlogConfig {

    private String configName;

    private String configValue;

    private Date createTime;

    private Date updateTime;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BlogConfig{" +
                "configName='" + configName + '\'' +
                ", configValue='" + configValue + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
