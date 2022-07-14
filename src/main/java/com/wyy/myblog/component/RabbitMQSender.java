package com.wyy.myblog.component;

import com.wyy.myblog.dto.BlogCRUDMsg;
import com.wyy.myblog.dto.QueueEnum;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * created by 伍猷煜 on 2022/7/12 14:45 星期二
 */
@Component
public class RabbitMQSender {

    @Resource
    private AmqpTemplate mAmqpTemplate;

    public void sendMsgIncrementBlogViews(long blogId) {
        mAmqpTemplate.convertAndSend(QueueEnum.QUEUE_BLOG_VIEWS.getExchange(),
                QueueEnum.QUEUE_BLOG_VIEWS.getRoutingKey(),
                blogId);
    }

    /**
     * 博客增、删、改，发送异步消息通知ES进行响应的增删改
     */
    public void sendMsgBlogES(BlogCRUDMsg blogCRUDMsg) {
        mAmqpTemplate.convertAndSend(QueueEnum.QUEUE_BLOG_ES.getExchange(),
                QueueEnum.QUEUE_BLOG_ES.getRoutingKey(),
                blogCRUDMsg);
    }
}
