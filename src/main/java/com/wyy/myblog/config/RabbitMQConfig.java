package com.wyy.myblog.config;

import com.wyy.myblog.dto.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by 伍猷煜 on 2022/7/11 16:51 星期一
 * RabbitMQ消息队列配置 用于配置交换器、队列及队列与交换器的绑定关系。
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 更新浏览量消息交换器
     * @return
     */
    @Bean
    public DirectExchange blogDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_BLOG_VIEWS.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 更新浏览量消息消费队列
     * @return
     */
    @Bean
    public Queue blogQueue() {
        return new Queue(QueueEnum.QUEUE_BLOG_VIEWS.getQueue());
    }

    /**
     * 将消费队列绑定到交换器
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding blogBinding(DirectExchange exchange, Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(QueueEnum.QUEUE_BLOG_VIEWS.getRoutingKey());
    }
}
