package com.qinglin.qlinvediomonitor.model;

import lombok.Data;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SensorReq
 * @Description
 * @date 2023/5/6 19:21
 */
@Data
public class SensorReq {

    /**
     * 湿度
     */
    private double humidity;

    /**
     * 温度
     */
    private double temperature;
}
