package com.wyy.myblog.util;

/**
 * created by 伍猷煜 on 2022/6/18 21:45 星期六
 * 用于生成Rest返回结果的工具类
 */
public class ResultUtil {

    private static final int DEFAULT_SUCCESS_CODE = 200;
    private static final int DEFAULT_FAIL_CODE = 500;
    private static final String DEFAULT_SUCCESS_MSG = "success";
    private static final String DEFAULT_FAIL_MSG = "fail";

    public static Result<?> success(){
        return new Result<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG);
    }

    // <T> Result<T> fun(T data) 返回的是泛型类型，这种方式更具体些
    // 调用该方法 ctrl + p会提示 ? extends Object data
    public static <T> Result<T> success(T data){
        return new Result<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, data);
    }

    // Result<?> fun(Object data) 返回的是Object泛型类型，这两种方式效果一样
    public static Result<?> success(String msg, Object data){
        return new Result<>(DEFAULT_SUCCESS_CODE, msg, data);
    }

    public static Result<?> fail() {
        return new Result<>(DEFAULT_FAIL_CODE, DEFAULT_FAIL_MSG);
    }

    public static Result<?> fail(String msg) {
        return new Result<>(DEFAULT_FAIL_CODE, msg);
    }

    public static Result<?> fail(int code, String msg) {
        return new Result<>(code, msg);
    }
}
