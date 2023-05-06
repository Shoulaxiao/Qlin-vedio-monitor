package com.qinglin.qlinvediomonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MyConfiguration
 * @Description
 * @date 2023/4/11 18:03
 */
@Configuration
public class MyConfiguration
{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}
