package com.qinglin.qlinvediomonitor.model;

import lombok.Data;
import org.bytedeco.javacv.Frame;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname FrameResult
 * @Description
 * @date 2023/5/3 12:12
 */
@Data
public class FrameResult {

    private Frame frame;

    /**
     * 检测是否成功
     */
    private boolean success;

    public FrameResult(Frame frame, boolean success) {
        this.frame = frame;
        this.success = success;
    }
}
