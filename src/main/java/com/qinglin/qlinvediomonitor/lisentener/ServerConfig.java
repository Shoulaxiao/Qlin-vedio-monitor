package com.qinglin.qlinvediomonitor.lisentener;

import com.qinglin.qlinvediomonitor.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {


    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int serverPort = event.getWebServer().getPort();
        String applicationName = event.getApplicationContext().getApplicationName();
        try {
            String IP = InetAddress.getLocalHost().getHostAddress();
            SystemUtils.WEB_STATIC_IMG = "http://" + IP + ":" + serverPort + applicationName + "/video/preViewImages/";
            log.info("preFix={}", SystemUtils.WEB_STATIC_IMG);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
