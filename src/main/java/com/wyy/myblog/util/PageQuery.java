package com.wyy.myblog.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/16 21:16 星期四
 * 分页查询参数
 */
public class PageQuery extends LinkedHashMap<String, Object> {

    // 当前页码
    private int page;

    // 每页条数
    private int limit;

    public PageQuery(Map<String, Object> params) {
        this.putAll(params);
        // 保存页码和每页条数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        // 当前页第一条记录的记录号
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
