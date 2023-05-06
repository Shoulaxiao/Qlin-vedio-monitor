package com.qinglin.qlinvediomonitor.model;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
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

    /**
     * 检测结果类型
     */
    private VideoTypeEnum videoType;

    public FrameResult() {
    }

    public FrameResult(Frame frame, boolean success, VideoTypeEnum videoType) {
        this.frame = frame;
        this.success = success;
        this.videoType = videoType;
    }
}
