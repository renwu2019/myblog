package com.wyy.myblog.component;

import com.wyy.myblog.dto.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
