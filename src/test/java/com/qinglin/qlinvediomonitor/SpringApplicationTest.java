package com.qinglin.qlinvediomonitor;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SpringApplicationTest
 * @Description
 * @date 2023/5/3 11:47
 */
@Slf4j
public class SpringApplicationTest extends QlinVedioMonitorApplicationTests{


    @Test
    public void readXml() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("haarcascade_frontalface_alt.xml");
        System.out.println(classPathResource.getFile().getAbsolutePath());
    }

    @Test
    public void testCamera1() throws Exception {

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
