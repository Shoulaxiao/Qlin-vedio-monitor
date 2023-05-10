package com.qinglin.qlinvediomonitor.lisentener;

import com.qinglin.qlinvediomonitor.model.InfoEvent;
import com.qinglin.qlinvediomonitor.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SensorAndAudioEventListener implements ApplicationListener<InfoEvent> {

    @Override
    public void onApplicationEvent(InfoEvent event) {
        try {
            ConcurrentHashMap<String,WebSocketServer> webSocketMap = WebSocketServer.getWebSocketMap();
            for (Map.Entry<String,WebSocketServer> entry:webSocketMap.entrySet()){
                entry.getValue().sendMessage(event.getMsg())    ;
            }
        } catch (IOException e) {
            log.error("事件消费异常");
        }
    }

}
