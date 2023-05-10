package com.qinglin.qlinvediomonitor.controller;

import com.alibaba.fastjson2.JSON;
import com.qinglin.qlinvediomonitor.common.SingleResult;
import com.qinglin.qlinvediomonitor.enums.CommandTypeEnum;
import com.qinglin.qlinvediomonitor.model.AudioInfoReq;
import com.qinglin.qlinvediomonitor.model.InfoEvent;
import com.qinglin.qlinvediomonitor.model.SocketResDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname AudioInfoController
 * @Description
 * @date 2023/5/10 14:56
 */
@RequestMapping(value = "/api/audio")
@Controller
@Slf4j
public class AudioInfoController {


    @Resource
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/audio")
    @ResponseBody
    public SingleResult<String> audio(@RequestBody AudioInfoReq req) {
        log.info("获取到的电位值为：{}", req.getElectricValue());
        SocketResDTO socketResDTO = new SocketResDTO();
        socketResDTO.setUniKey(UUID.randomUUID().toString());
        socketResDTO.setResult(JSON.toJSONString(req));
        socketResDTO.setCmdEnterType(CommandTypeEnum.AUDIO_INFO.getCode());
        try {
            eventPublisher.publishEvent(new InfoEvent(this, JSON.toJSONString(socketResDTO)));
        } catch (Exception e) {
            log.error("发送到前端展示失败", e);
            return new SingleResult<>(null, false, StringUtils.EMPTY, "发送到前端展示失败");
        }
        return new SingleResult<>(null, true, StringUtils.EMPTY, StringUtils.EMPTY);
    }
}
