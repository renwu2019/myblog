package com.wyy.myblog.listen;

import com.wyy.myblog.dao.BlogMapper;
import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/7/4 18:12 星期一
 * SpringBoot+Redis简单实现文章浏览量记录 https://blog.csdn.net/wl_Honest/article/details/123082306
 *
 */
@Component
@EnableScheduling
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ListenHandler  {

    private static final Logger log = LoggerFactory.getLogger(ListenHandler.class);

    @Resource
    private BlogMapper mBlogMapper;

    @Resource
    private RedisUtil mRedisUtil;

    @Value("${redis.key.prefix.pv}")
    private String REDIS_KEY_PREFIX_PV;

    @Value("${redis.key.prefix.uv}")
    private String REDIS_KEY_PREFIX_UV;


    @PostConstruct
    public void init() {
        log.info("数据初始化开始...");
        // 将数据库中的数据写入redis
        // 这里就不太方便全部放进来，特别是数据量特别大的时候，还是请求的时候未命中缓存时再加入到缓存比较好
    }

    /**
     * key过期回调
     * @param message
     * @param pattern
     */


    /**
     * 关闭时操作
     */
    @PreDestroy
    public void afterDestroy() {
        log.info("开始关闭...");
        //将redis中的数据写入数据库
        Map<Object, Object> blogPV = mRedisUtil.hEntryGet(REDIS_KEY_PREFIX_PV);
        writeNum(blogPV, REDIS_KEY_PREFIX_PV);
        log.info("redis写入数据库完毕");
    }

    /**
     * 定时将redis数据存到数据库 避免热点数据不过期，导致长时间未同步到数据库
     */
    @Scheduled(cron = "0 */1 * * * ?")  // 每分钟执行一次
    public void updateNum() {
        log.info("周期任务开始执行...");
        Map<Object, Object> blogPV = mRedisUtil.hEntryGet(REDIS_KEY_PREFIX_PV);
        writeNum(blogPV, REDIS_KEY_PREFIX_PV);
        log.info("周期任务执行完毕,redis写入数据库完毕, 写入数据：{}", blogPV.entrySet().toString());
    }

    private void writeNum(Map<Object, Object> map, String fieldName) {
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String num = entry.getValue().toString();
            log.debug("key = {}, value = {}", key, num);
            Long id = Long.valueOf(key);
            Long blogViews = Long.valueOf(num);
            Blog blog = mBlogMapper.selectByPrimaryKey(id);
            if (blog != null) {
                blog.setBlogViews(blogViews);
                // 更新数据库
                mBlogMapper.updateByPrimaryKeySelective(blog);
            }
        }
        log.info("{} 更新完毕", fieldName);
    }


}
