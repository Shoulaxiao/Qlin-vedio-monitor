package com.qinglin.qlinvediomonitor.model;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SensorEvent
 * @Description
 * @date 2023/5/6 22:17
 */
public class SensorEvent extends ApplicationEvent {

    private static final long serialVersionUID = -8451336151994081480L;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SensorEvent(Object source) {
        super(source);
    }

    public SensorEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public SensorEvent(Object source, Clock clock, String msg) {
        super(source, clock);
        this.msg = msg;
    }
}
