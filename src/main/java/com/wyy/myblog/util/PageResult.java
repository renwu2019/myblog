package com.wyy.myblog.util;

import java.io.Serializable;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/6/16 21:16 星期四
 * 分页查询结果
 */
public class PageResult<T> implements Serializable {

    // 总记录数
    private int totalCount;

    // 页大小
    private int pageSize;

    // 总页数
    private int totalPage;

    // 当前页
    private int currPage;

    // 当前页数据
    private List<T> list;

    // 在使用Hutool的jsonToBean传入new TypeReference<PageResult<BlogComment>>() {}时，
    // 会调用newInstanceIfPossible，该方法尝试遍历并调用此类的所有构造方法，直到构造成功并返回，
    // 如果没有构造成功的，直接返回null，导致json转带泛型参数的bean失败。因此，这里需要无参构造函数。
    public PageResult() {}

    public PageResult(int totalCount, int pageSize, int currPage, List<T> list) {
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
