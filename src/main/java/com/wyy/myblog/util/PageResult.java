package com.wyy.myblog.util;

import java.io.Serializable;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 21:16 星期四
 * 分页查询结果
 */
public class PageResult implements Serializable {

    // 总记录数
    private int totalCount;

    // 页大小
    private int pageSize;

    // 总页数
    private int totalPage;

    // 当前页
    private int currPage;

    // 当前页数据
    private List<?> list;

    public PageResult(int totalCount, int pageSize, int currPage, List<?> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = (totalCount + pageSize - 1) / pageSize;  // 向上取正
        this.currPage = currPage;
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
