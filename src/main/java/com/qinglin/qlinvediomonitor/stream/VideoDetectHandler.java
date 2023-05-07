package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.stream.detect.DetectService;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.Frame;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname VideoDetectHandler
 * @Description
 * @date 2023/5/3 12:05
 */
@Slf4j
public class VideoDetectHandler {

    private HandlerNode cur;
    private HandlerNode head;


    public VideoDetectHandler addHandler(DetectService detectService) {
        if (cur == null) {
            head = cur = new HandlerNode(detectService);
        } else {
            cur.setNext(new HandlerNode(detectService));
            cur = cur.next;
        }
        return this;
    }

    public void release(){
        HandlerNode cur = head;
        while (cur != null){
            cur.release();
            cur = cur.next;
        }
    }


    public FrameResult detect(Frame frame) {
        FrameResult frameResult = null;
        cur = head;
        while (cur != null) {
            frameResult = cur.handle(frame);
            if (frameResult.isSuccess()) {
                // 检测成功，直接返回
                return frameResult;
            }
            cur = cur.next;
        }
        return frameResult;
    }

    public FrameResult detect(Mat frontFrame, Mat afterFrame) {
        FrameResult frameResult = null;
        cur = head;
        while (cur != null) {
            frameResult = cur.handle(frontFrame,afterFrame);
            if (frameResult.isSuccess()) {
                // 检测成功，直接返回
                return frameResult;
            }
            cur = cur.next;
        }
        return frameResult;
    }


    static class HandlerNode {
        private DetectService detectService;
        private HandlerNode next;

        public HandlerNode(DetectService detectService) {
            this.detectService = detectService;
        }

        public FrameResult handle(Frame frame) {
            return detectService.convert(frame);
        }

        public FrameResult handle(Mat frontFrame,Mat afterFrame) {
            return detectService.detect(frontFrame,afterFrame);
        }

        public boolean release(){
            try {
                detectService.releaseOutputResource();
            }catch (Exception e){
                return false;
            }
            return true;
        }
        public void setNext(HandlerNode next) {
            this.next = next;
        }
    }
}
