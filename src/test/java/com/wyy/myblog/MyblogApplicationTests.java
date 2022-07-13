package com.wyy.myblog;

import com.rabbitmq.client.AMQP;
import com.wyy.myblog.dao.*;
import com.wyy.myblog.dto.QueueEnum;
import com.wyy.myblog.entity.Blog;
import com.wyy.myblog.entity.BlogCategory;
import com.wyy.myblog.util.PageQuery;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class MyblogApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyblogApplicationTests.class);

    @Resource
    private BlogMapper mBlogMapper;

    @Resource
    private BlogCommentMapper mBlogCommentMapper;

    @Resource
    private BlogCategoryMapper mBlogCategoryMapper;

    @Resource
    private BlogTagMapper mBlogTagMapper;

    @Resource
    private BlogLinkMapper mBlogLinkMapper;

    @Resource
    private StringRedisTemplate mStringRedisTemplate;

    @Resource
    RedisTemplate<String, Object> mRedisTemplate;

    @Resource
    AmqpTemplate mAmqpTemplate;

    @Test
    public void testRabbitMQProducer() {
        mAmqpTemplate.convertAndSend(
                QueueEnum.QUEUE_BLOG_VIEWS.getExchange(),
                QueueEnum.QUEUE_BLOG_VIEWS.getRoutingKey(),
                "hello");
    }

    @RabbitListener(queues = "myblog.blog.blogviews")
    public void testRabbitMQConsumer(String msg, Message message) {
        LOGGER.debug("msg: {}, message: {}", msg, message);
        // mAmqpTemplate.receiveAndConvert();
    }

    @Test
    public void testRedis() {
        mStringRedisTemplate.opsForValue().set("a", "1");
        System.out.println(mStringRedisTemplate.opsForValue().get("a"));
        mStringRedisTemplate.opsForValue().increment("a");
        System.out.println(mStringRedisTemplate.opsForValue().get("a"));

        // put报错： java.lang.Integer cannot be cast to java.lang.String
        // mStringRedisTemplate.opsForHash().put("b", "b1", 1);
        // StringRedisTemplate 的hashValue因为使用了StringRedisSerializer，序列化时调用下面这个方法
        // public byte[] serialize(@Nullable String string) ，如果传入整型就会报错了。
        mStringRedisTemplate.opsForHash().put("b", "b1", "1");
        mStringRedisTemplate.opsForHash().increment("b", "b1", 1);
        System.out.println("hash增：" + mStringRedisTemplate.opsForHash().get("b", "b1"));

        mRedisTemplate.opsForHash().put("c", "c1", 1);
        // mRedisTemplate.opsForHash().put("c", "b1", "1");
        mRedisTemplate.opsForHash().increment("c", "c1", 1);
        // SerializationException: Cannot deserialize
        // 解决：在redisConfig中设置HashValue序列化：redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        System.out.println("hash增：" + mRedisTemplate.opsForHash().get("c", "c1"));

        // Redis几种序列化方式：https://www.csdn.net/tags/Mtzakg5sMzMzNTEtYmxvZwO0O0OO0O0O.html
    }

    @Test
    void contextLoads() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("page", 1);
        map.put("limit", 2);
        map.put("keyword", "hh");
        PageQuery pageQuery = new PageQuery(map);
        System.out.println(mBlogMapper.getTotalBlogs(pageQuery));
    }

    @Test
    public void testBlogMapper() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", 2);
        params.put("limit", 8);  // 分页大小为8
        params.put("blogStatus", 1);  // 博客状态必须为发布状态
        PageQuery pageQuery = new PageQuery(params);
        // 如果没有的话是返回一个空列表，而不是null
        List<Blog> blogList = mBlogMapper.getBlogList(pageQuery);
        System.out.println(blogList);
    }

    @Test
    public void testBlogCommentMapper() {
        Map<String, Integer> params = new HashMap<>();
        params.put("blogId", 4);
        // 传字符串也是可行的，mysql会将字符串转为整型再比较，但不提倡:)
        // Map<String, String> params = new HashMap<>();
        // params.put("blogId", "4a");
        System.out.println("评论数量：" + mBlogCommentMapper.getTotalBlogComments(params));

        System.out.println("根据主键查询的评论：" + mBlogCommentMapper.selectByPrimaryKey(29L));
    }

    @Test
    public void testBlogCategoryMapper() {
        System.out.println("类别数：" + mBlogCategoryMapper.getTotalBlogCategories(null));

        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryId(123);
        blogCategory.setCategoryName("好好");
        blogCategory.setCategoryIcon("/admin/dd");
        blogCategory.setCategoryRank(34);
        blogCategory.setIsDeleted((byte) 0);
        blogCategory.setCreateTime(new Date());
        mBlogCategoryMapper.insert(blogCategory);
    }

    @Test
    public void testBlogTagMapper() {
        System.out.println("标签数：" + mBlogTagMapper.getTotalBlogTags(null));
    }

    @Test
    public void testBlogLinkMapper() {
        System.out.println("友链数：" + mBlogLinkMapper.getTotalBlogLinks(null));
    }
}
