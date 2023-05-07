package com.qinglin.qlinvediomonitor.lisentener;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.model.RecordEvent;
import com.qinglin.qlinvediomonitor.stream.RecordRtmpSaveMp4;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Mp4RecorderListener implements ApplicationListener<RecordEvent> {

    private boolean started;

    @Resource
    private RecordRtmpSaveMp4 recordRtmpSaveMp4;

    @Override
    public void onApplicationEvent(RecordEvent event) {
        if (event.getSource() == null) {
            return;
        }
        if (event.getSource() instanceof FrameResult){
            FrameResult frameResult = (FrameResult) event.getSource();
            //开始录制
            if (frameResult.isMove() && !started) {
                try {
                    started = true;
                    recordRtmpSaveMp4.start(frameResult);
                } catch (FrameRecorder.Exception e) {
                    log.error("开始录制，启动失败", e);
                    started = false;
                }
            }
            //结束录制
            if (frameResult.isEnd() && started) {
                try {
                    started = false;
                    recordRtmpSaveMp4.end(frameResult);
                } catch (FrameRecorder.Exception e) {
                    log.error("结束录制，失败", e);
                    started = true;
                }
            }
            // 录制中
            if (started) {
                try {
                    recordRtmpSaveMp4.process(frameResult);
                } catch (FrameRecorder.Exception e) {
                    log.error("录制中异常", e);
                }
            }
        }
    }
}
