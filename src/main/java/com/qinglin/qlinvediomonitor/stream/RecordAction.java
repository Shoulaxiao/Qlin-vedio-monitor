package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import org.bytedeco.javacv.FrameRecorder;

public interface RecordAction {

    void start(FrameResult frameResult) throws FrameRecorder.Exception;

    void end(FrameResult frameResult) throws FrameRecorder.Exception;

    void  process(FrameResult frameResult) throws FrameRecorder.Exception;

}
