package com.qinglin.qlinvediomonitor.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname BasePageReq
 * @Description
 * @date 2023/4/11 10:49
 */
@Data
public class BasePageReq implements Serializable {
    private static final long serialVersionUID = 4673507091130921780L;

    private Integer page;
    private Integer pageSize;
}
