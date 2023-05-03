package com.qinglin.qlinvediomonitor.stream;

import lombok.Builder;
import lombok.Data;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname ActionConfig
 * @Description
 * @date 2023/5/3 09:45
 */
@Data
@Builder
public class ActionConfig {

    private int grabSeconds;
    private String pushUrl;
    private String sourceUrl;
    private int cameraIndex = -1;
}
