package com.qinglin.qlinvediomonitor.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author shoulaxiao
 * @version 1.0.0
 * @ClassName ErrorConfig.java
 * @Description
 * @createTime 2021年06月15日 17:19:00
 */
@Configuration
public class ErrorConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        ErrorPage notFoundError = new ErrorPage(HttpStatus.NOT_FOUND,"/index.html");
        errorPageRegistry.addErrorPages(notFoundError);
    }
}
