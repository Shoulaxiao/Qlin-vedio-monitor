package com.qinglin.qlinvediomonitor.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qinglin.qlinvediomonitor.enums.CommandTypeEnum;
import com.qinglin.qlinvediomonitor.model.SensorEvent;
import com.qinglin.qlinvediomonitor.model.SocketReqDTO;
import com.qinglin.qlinvediomonitor.model.SocketResDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname WebSocketServer
 * @Description
 * @date 2023/5/6 19:29
 */

@Slf4j
@ServerEndpoint("/wsserver/{userId}")//userId：地址的111就是这个userId"ws://localhost:8899/wsserver/111"
@Component
public class WebSocketServer implements ApplicationListener<SensorEvent> {

    /**
     * 用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            //加入set中
        } else {
            webSocketMap.put(userId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        System.out.println("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            System.out.println("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        System.out.println("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("用户消息:" + userId + ",报文:" + message);
        if (StringUtils.isNotBlank(message)) {
            try {
                //解析发送的报文
                SocketReqDTO socketReqDTO = JSON.parseObject(message, SocketReqDTO.class);

                if (socketReqDTO != null && socketReqDTO.getCmdEnterType().equals(CommandTypeEnum.HEART_BEAT.getCode())) {
                    sendMessage("^s^h&b^" + System.currentTimeMillis(), CommandTypeEnum.HEART_BEAT, socketReqDTO.getUniKey());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(String message, CommandTypeEnum etermType, String uniKey) throws IOException {
        SocketResDTO socketResDTO = new SocketResDTO();
        socketResDTO.setUniKey(UUID.randomUUID().toString());
        socketResDTO.setResult(message);
        socketResDTO.setCmdEnterType(etermType.getCode());
        this.sendMessage(JSON.toJSONString(socketResDTO));
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        System.out.println("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            System.out.println("用户" + userId + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


    @Override
    public void onApplicationEvent(SensorEvent event) {
        try {
            this.sendMessage(event.getMsg());
        } catch (IOException e) {
            log.error("事件消费异常");
        }
    }
}
