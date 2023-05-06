package com.qinglin.qlinvediomonitor.model;

import lombok.Data;

import java.util.Map;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SocketResDTO
 * @Description
 * @date 2023/5/6 19:46
 */
@Data
public class SocketResDTO {

    /**
     * 唯一key
     */
    private String uniKey;

    /**
     * 指令结果
     */
    private String result;

    /**携带的特殊参数*/
    private Map<Object, Object> extInfo;
    /**
     * 指令输入类型
     * @see com.qinglin.qlinvediomonitor.enums.CommandTypeEnum
     */
    private String cmdEnterType;
}
