package com.qinglin.qlinvediomonitor.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname AudioInfoReq
 * @Description
 * @date 2023/5/10 14:57
 */
@Data
public class AudioInfoReq implements Serializable {
    private static final long serialVersionUID = 1767421771095612577L;

    private Integer electricValue;
}
