package com.wyy.myblog.service;

import com.wyy.myblog.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

/**
 * created by 伍猷煜 on 2022/6/14 21:33 星期二
 */
public interface AdminUserService {

    AdminUser login(String userName, String password);

    AdminUser getUserDetailById(Integer userId);

    Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

    Boolean updatePassword(Integer loginUserId, String oldPassword, String newPassword);
}
