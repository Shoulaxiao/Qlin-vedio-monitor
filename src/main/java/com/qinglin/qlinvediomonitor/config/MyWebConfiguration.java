package com.qinglin.qlinvediomonitor.config;

import com.qinglin.qlinvediomonitor.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MyConfiguration
 * @Description
 * @date 2023/4/10 18:20
 */
@Configuration
public class MyWebConfiguration implements WebMvcConfigurer {

    private  final String IMAGE_URL = "";

    @Resource
    private LoginInterceptor loginInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/api/oauth/apply",
//                        "/oauth/callback",
//                        "/api/oauth/logout",
//                        "/monitor.jsp",
//                        "/api/wx/**");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(false)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 加载静态资源，访问路径不需要加前缀 http://127.0.0.1:8080/1.jpg
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/static/video/preViewImages/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 挂在本地目录，访问路径需要加前缀 http://127.0.0.1:8080/image/1.jpg
//        registry.addResourceHandler("/video/**").addResourceLocations("IMAGE_URL");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
