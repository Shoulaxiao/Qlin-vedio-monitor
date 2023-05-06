package com.qinglin.qlinvediomonitor.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SocketReqDTO
 * @Description
 * @date 2023/5/6 21:05
 */
@Data
public class SocketReqDTO implements Serializable {
    private static final long serialVersionUID = -1820518226659950736L;
    private String uniKey;
    private String cmd;
    private String cmdEnterType;


}
