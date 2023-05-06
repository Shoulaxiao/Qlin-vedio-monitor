package com.qinglin.qlinvediomonitor.controller;

import com.alibaba.fastjson2.JSON;
import com.qinglin.qlinvediomonitor.common.SingleResult;
import com.qinglin.qlinvediomonitor.enums.CommandTypeEnum;
import com.qinglin.qlinvediomonitor.model.SensorReq;
import com.qinglin.qlinvediomonitor.model.SocketResDTO;
import com.qinglin.qlinvediomonitor.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SensorInfoController
 * @Description
 * @date 2023/5/6 19:19
 */
@RequestMapping(value = "/api/sensor")
@Controller
@Slf4j
public class SensorInfoController {


    @Resource
    private WebSocketServer webSocketServer;

    @RequestMapping(value = "/send")
    @ResponseBody
    public SingleResult<String> send(@RequestBody SensorReq sensorReq) {
        log.info("获取到的[温度]={} ℃,[湿度]={}%", sensorReq.getTemperature(), sensorReq.getHumidity());

        SocketResDTO socketResDTO = new SocketResDTO();
        socketResDTO.setUniKey(UUID.randomUUID().toString());
        socketResDTO.setResult(JSON.toJSONString(sensorReq));
        socketResDTO.setCmdEnterType(CommandTypeEnum.SENSOR_INFO.getCode());
        try {
            webSocketServer.sendMessage(JSON.toJSONString(socketResDTO));
        } catch (IOException e) {
            log.error("发送传感器信息到客户端失败", e);
        }
        return new SingleResult<>(null, true, StringUtils.EMPTY, StringUtils.EMPTY);
    }
}
