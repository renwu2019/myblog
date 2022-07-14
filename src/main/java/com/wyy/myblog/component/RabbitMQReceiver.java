package com.wyy.myblog.component;

import com.wyy.myblog.dto.BlogCRUDEnum;
import com.wyy.myblog.dto.BlogCRUDMsg;
import com.wyy.myblog.service.EsBlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * created by 伍猷煜 on 2022/7/12 14:45 星期二
 */
@Component
public class RabbitMQReceiver {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQReceiver.class);

    @Value("${redis.key.prefix.pv}")
    private String REDIS_KEY_PREFIX_PV;

    @Resource
    private RedisOperator mRedisOperator;

    @Resource
    private EsBlogService mEsBlogService;

    @RabbitListener(queues = "myblog.blog.blogviews")
    public void receiveMsgIncrementBlogViews(long blogId) {
        String key = String.valueOf(blogId);
        mRedisOperator.hIncr(REDIS_KEY_PREFIX_PV, key, 1);  // 浏览量加1
    }

    @RabbitListener(queues = "myblog.blog.es")
    public void receiveMsgBlogEs(BlogCRUDMsg blogCRUDMsg) {
        log.info("EsBlog：{}", blogCRUDMsg);
        int type = blogCRUDMsg.getType();
        List<Long> blogIds = blogCRUDMsg.getBlogIds();
        if (CollectionUtils.isEmpty(blogIds)) {
            return;
        }
        if (type == BlogCRUDEnum.SAVE.getType()) { // 创建EsBlog
            mEsBlogService.create(blogIds.get(0));
        } else if (type == BlogCRUDEnum.DELETE.getType()) { // 删除EsBlog
            mEsBlogService.delete(blogIds);
        } else if (type == BlogCRUDEnum.UPDATE.getType()) {  // 更新EsBlog
            mEsBlogService.delete(blogIds.get(0));
            mEsBlogService.create(blogIds.get(0));
        }
    }
}
