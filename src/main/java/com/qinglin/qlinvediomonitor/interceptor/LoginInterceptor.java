package com.qinglin.qlinvediomonitor.interceptor;

import com.qinglin.qlinvediomonitor.utils.SystemUtils;
import com.qinglin.qlinvediomonitor.utils.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname LoginInterceptor
 * @Description
 * @date 2023/4/11 19:21
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
