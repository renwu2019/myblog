package com.wyy.myblog.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * created by 伍猷煜 on 2022/6/15 14:50 星期三
 * 错误页面
 */
@Controller
public class ErrorPageController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorPageController.class);

    @Resource
    private ErrorAttributes mErrorAttributes;

    public static ErrorPageController errorPageController;

    private final static String ERROR_PATH = "/error";

    // 使用注入的ErrorAttributes
    public ErrorPageController() {
        if (errorPageController == null) {
            errorPageController = new ErrorPageController(mErrorAttributes);
        }
    }

    // 使用自定义的ErrorAttributes
    public ErrorPageController(ErrorAttributes errorAttributes) {
        this.mErrorAttributes = errorAttributes;
    }


    /**
     * 自定义错误页面
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (HttpStatus.BAD_REQUEST == status) {
            return new ModelAndView("error/error_400");
        } else if (HttpStatus.NOT_FOUND == status) {
            return new ModelAndView("error/error_404");
        } else {
            return new ModelAndView("error/error_5xx");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Accept为任意数据格式时 返回错误信息
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.defaults();
        if (includeStackTrace) {
            errorAttributeOptions.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        return this.mErrorAttributes.getErrorAttributes(webRequest, errorAttributeOptions);
    }

}
