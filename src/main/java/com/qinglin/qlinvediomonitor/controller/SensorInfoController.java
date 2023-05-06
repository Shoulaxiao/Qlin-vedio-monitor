package com.qinglin.qlinvediomonitor.controller;

import com.qinglin.qlinvediomonitor.common.SingleResult;
import com.qinglin.qlinvediomonitor.model.SensorReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @RequestMapping(value = "/send")
    @ResponseBody
    public SingleResult<String> send(@RequestBody SensorReq sensorReq) {
        log.info("获取到的[温度]={} ℃,[湿度]={}%", sensorReq.getTemperature(), sensorReq.getHumidity());
        return new SingleResult<>(null, true, StringUtils.EMPTY, StringUtils.EMPTY);
    }
}
