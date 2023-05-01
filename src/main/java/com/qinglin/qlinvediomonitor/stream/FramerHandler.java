package com.qinglin.qlinvediomonitor.stream;

import org.bytedeco.javacv.Frame;

public interface FramerHandler {


    Frame addRealTime(Frame frame);
}
