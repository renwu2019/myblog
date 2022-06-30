package com.wyy.myblog.config;

import com.wyy.myblog.intercepter.AdminLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * created by 伍猷煜 on 2022/6/19 21:53 星期日
 */

@Configuration
public class MyBlogWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private AdminLoginInterceptor mAdminLoginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径
        registry.addInterceptor(mAdminLoginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")  // 排除登录页和静态资源
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 对/upload/下资源的访问会去寻找file:Constants.FILE_UPLOAD_DIC目录下的文件
        registry.addResourceHandler(Constants.RESOURCE_PATH_PREFIX + "**")
                .addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }

    // 解决跨域问题
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedHeaders("*")
    //             .allowedMethods("*")
    //             .maxAge(1800)
    //             .allowedOrigins("*");
    // }

}
