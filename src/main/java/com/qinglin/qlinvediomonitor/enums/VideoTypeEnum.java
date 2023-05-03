package com.qinglin.qlinvediomonitor.enums;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname VideoTypeEnum
 * @Description
 * @date 2023/5/3 11:14
 */
public enum VideoTypeEnum {
    /**
     * 画面抖动
     */
    PICTURE_MOVE(0, "画面抖动"),
    /**
     * 人物运动
     */
    CHARACTER_MOVE(1, "人物运动");

    private Integer code;
    private String desc;

    VideoTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
