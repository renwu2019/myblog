package com.wyy.myblog.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * created by 伍猷煜 on 2022/7/12 14:45 星期二
 */
@Component
public class RabbitMQReceiver {

    @Value("${redis.key.prefix.pv}")
    private String REDIS_KEY_PREFIX_PV;

    @Resource
    private RedisOperator mRedisOperator;

    @RabbitListener(queues = "myblog.blog.blogviews")
    public void receiveMsgIncrementBlogViews(long blogId) {
        String key = String.valueOf(blogId);
        mRedisOperator.hIncr(REDIS_KEY_PREFIX_PV, key, 1);  // 浏览量加1
    }
}
