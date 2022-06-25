package com.wyy.myblog.service.impl;

import com.wyy.myblog.dao.BlogConfigMapper;
import com.wyy.myblog.entity.BlogConfig;
import com.wyy.myblog.service.ConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by 伍猷煜 on 2022/6/22 19:42 星期三
 */

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Resource
    private BlogConfigMapper mBlogConfigMapper;


    @Override
    public Map<String, String> getAllConfigs() {
        // 获取所有的配置，并封装成map
        List<BlogConfig> configs = mBlogConfigMapper.selectAll();
        return configs.stream().collect(Collectors.toMap(BlogConfig::getConfigName, BlogConfig::getConfigValue));
    }

    @Override
    @Transactional
    public Boolean updateConfig(Map<String, String> configs) {
        boolean success = false;
        for (Map.Entry<String, String> config : configs.entrySet()) {
            String configName = config.getKey();
            String configValue = config.getValue();
            BlogConfig blogConfig = mBlogConfigMapper.selectByPrimaryKey(configName);
            if (blogConfig != null) {
                blogConfig.setConfigValue(configValue);
                blogConfig.setUpdateTime(new Date());
                // return false; 这样无法保证是一个事务，后面终止了，但前面的还是会执行
                // throw new RuntimeException(); 可以保证是一个事务，但是无法优雅进行处理
                // 不过这个部分也不是强原子性的更新，允许部分更新成功。
                // 那索性只要有一个更新成功，就算成功呗。
                if (mBlogConfigMapper.updateByPrimaryKeySelective(blogConfig) > 0) {
                    success = true;
                }
            } else {
                // 初始化配置
                blogConfig = new BlogConfig();
                blogConfig.setConfigName(configName);
                blogConfig.setConfigValue(configValue);
                if (mBlogConfigMapper.insertSelective(blogConfig) > 0) {
                    success = true;
                }
            }
        }
        return success;
    }


}
