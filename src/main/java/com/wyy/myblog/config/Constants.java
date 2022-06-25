package com.wyy.myblog.config;

import java.io.File;

/**
 * created by 伍猷煜 on 2022/6/22 13:43 星期三
 */
public class Constants {
    // public static final String FILE_UPLOAD_DIC = "/opt/deploy/upload/";//上传文件的默认url前缀，根据部署设置自行修改

    // 提供对外访问的资源前缀
    public static final String RESOURCE_PATH_PREFIX = "/upload/";
    // 对外访问的资源实际目录
    public static final String FILE_UPLOAD_DIC =
            // 当前程序所在目录
            new File(System.getProperties().getProperty("user.dir"))
                    + File.separator + "myblog_upload" + File.separator;
}
