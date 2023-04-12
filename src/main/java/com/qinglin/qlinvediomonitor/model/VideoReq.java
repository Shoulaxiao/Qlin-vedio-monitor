package com.qinglin.qlinvediomonitor.model;

import com.qinglin.qlinvediomonitor.common.BasePageReq;
import lombok.Data;

import java.util.Date;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname VideoReq
 * @Description
 * @date 2023/4/11 15:00
 */
@Data
public class VideoReq extends BasePageReq {
    private static final long serialVersionUID = 3781601091236993609L;

    private Date date;
}
