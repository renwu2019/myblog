package com.wyy.myblog.service.impl;

import com.wyy.myblog.dao.AdminUserMapper;
import com.wyy.myblog.entity.AdminUser;
import com.wyy.myblog.service.AdminUserService;
import com.wyy.myblog.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by 伍猷煜 on 2022/6/14 21:37 星期二
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {


    @Resource
    private AdminUserMapper mAdminUserMapper;
    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public AdminUser login(String userName, String password) {
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        return mAdminUserMapper.login(userName, passwordMD5);
    }

    @Override
    public AdminUser getUserDetailById(Integer userId) {
        return mAdminUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = mAdminUserMapper.selectByPrimaryKey(loginUserId);
        if (adminUser == null) {
            return false;
        }
        adminUser.setLoginUserName(loginUserName);
        adminUser.setNickName(nickName);
        return mAdminUserMapper.updateByPrimaryKeySelective(adminUser) > 0;
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String oldPassword, String newPassword) {
        AdminUser adminUser = mAdminUserMapper.selectByPrimaryKey(loginUserId);
        if (adminUser == null) {
            return false;
        }
        String oldPasswordMD5 = MD5Util.MD5Encode(oldPassword, "UTF-8");
        if (!oldPasswordMD5.equals(adminUser.getLoginPassword())) {
            return false;
        }
        String newPasswordMD5 = MD5Util.MD5Encode(newPassword, "UTF-8");
        adminUser.setLoginPassword(newPasswordMD5);
        return mAdminUserMapper.updateByPrimaryKeySelective(adminUser) > 0;
    }


}
