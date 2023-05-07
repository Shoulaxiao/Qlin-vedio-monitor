package com.qinglin.qlinvediomonitor;


import org.bytedeco.javacv.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImgTest {



    public static void main(String[] args) throws  Exception, IOException {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("摄像头窗口");//新建一个窗口
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        while (true) {
            if (!canvas.isDisplayable()) {//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(-1);//退出
            }

            Frame frame = grabber.grab();
            canvas.showImage(frame);	//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
            Thread.sleep(50);	//50毫秒刷新一次图像
        }
    }


}
