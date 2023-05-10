package com.qinglin.qlinvediomonitor.enums;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname CommandTypeEnum
 * @Description
 * @date 2023/5/6 19:47
 */
public enum CommandTypeEnum {


    /**
     * SENSOR_INFO
     */
    HEART_BEAT("HEART_BEAT", "心跳"),
    SENSOR_INFO("SENSOR_INFO", "传感器信息"),
    AUDIO_INFO("AUDIO_INFO", "声音信息"),
    ;

    private String code;
    private String desc;

    CommandTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
