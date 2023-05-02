package com.qinglin.qlinvediomonitor;

import org.bytedeco.javacv.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname CameriaTest
 * @Description
 * @date 2023/4/19 23:12
 */
public class CameraTest {

    public static void main(String[] args) throws FrameGrabber.Exception {
        try {
            //VideoInputFrameGrabber grabber = VideoInputFrameGrabber.createDefault(0);
            OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
            grabber.start();//开始获取摄像头数据
            final CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
            canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            canvas.setAlwaysOnTop(true);

            while (true) {
                if (!canvas.isDisplayable()) {//窗口是否关闭
                    grabber.stop();//停止抓取
                    System.exit(-1);//退出
                }

                Frame frame = grabber.grab();
                canvas.showImage(frame);//获取摄像头图像并放到窗口上显示，frame是一帧视频图像
                Thread.sleep(30);//50毫秒刷新一次图像
            }


        }catch (FrameGrabber.Exception e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
