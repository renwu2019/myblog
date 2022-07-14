package com.wyy.myblog.dto;

/**
 * created by 伍猷煜 on 2022/7/11 16:37 星期一
 * 消息队列枚举配置 用于消息队列的常量定义，包括交换机名称、队列名称、路由键名称。
 */
public enum QueueEnum {
    // 交换器
    EXCHANGE_BLOG("myblog.blog.direct", "", ""),
    // 队列
    QUEUE_BLOG_ES(EXCHANGE_BLOG.getExchange(), "myblog.blog.es", "myblog.blog.es"),
    QUEUE_BLOG_VIEWS(EXCHANGE_BLOG.getExchange(), "myblog.blog.blogviews", "myblog.blog.blogviews");

    /**
     * 交换器
     */
    private String exchange;

    /**
     * 队列名称
     */
    private String queue;

    /**
     * 路由键
     */
    private String routingKey;

    QueueEnum(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
