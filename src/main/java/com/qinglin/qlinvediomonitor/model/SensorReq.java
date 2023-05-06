package com.qinglin.qlinvediomonitor.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SensorReq
 * @Description
 * @date 2023/5/6 19:21
 */
@Data
public class SensorReq implements Serializable {

    private static final long serialVersionUID = -9059740337596296878L;
    /**
     * 湿度
     */
    private String humidity;

    /**
     * 温度
     */
    private String temperature;
}
