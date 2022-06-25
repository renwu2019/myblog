package com.wyy.myblog.dao;

import com.wyy.myblog.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

/**
 * created by 伍猷煜 on 2022/6/14 17:04 星期二
 */
public interface AdminUserMapper {

    int insert(AdminUser adminUser);

    AdminUser login(@Param("userName") String userName, @Param("password") String password);

    AdminUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(AdminUser adminUser);

}
