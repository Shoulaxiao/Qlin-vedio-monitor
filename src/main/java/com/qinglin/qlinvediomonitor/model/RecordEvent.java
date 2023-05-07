package com.qinglin.qlinvediomonitor.model;

import org.springframework.context.ApplicationEvent;

public class RecordEvent extends ApplicationEvent {

    private FrameResult msg;

    public FrameResult getMsg() {
        return msg;
    }

    public void setMsg(FrameResult msg) {
        this.msg = msg;
    }

    public RecordEvent(Object source) {
        super(source);
    }
}
