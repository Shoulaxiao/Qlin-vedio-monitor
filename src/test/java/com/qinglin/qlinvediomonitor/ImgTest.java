package com.qinglin.qlinvediomonitor;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;

public class ImgTest {


    public static void main(String[] args) {
        opencv_core.Mat image = opencv_imgcodecs.imread("/home/shoulaxiao/Downloads/607f1f3d68_s.jpg");

    }
}
