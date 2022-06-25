package com.wyy.myblog.service;

import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/22 19:42 星期三
 */
public interface ConfigurationService {

    Map<String, String> getAllConfigs();

    Boolean updateConfig(Map<String, String> configs);
}
