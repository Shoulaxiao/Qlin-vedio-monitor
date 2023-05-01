package com.qinglin.qlinvediomonitor.stream.impl;

import com.qinglin.qlinvediomonitor.stream.FramerHandler;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.springframework.stereotype.Service;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FrameAddTimeHandler implements FramerHandler {


    static OpenCVFrameConverter.ToIplImage cv = new OpenCVFrameConverter.ToIplImage();
    Java2DFrameConverter converter = new Java2DFrameConverter();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Frame addRealTime(Frame frame) {
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        addSubtitle(bufferedImage);
        frame.image = converter.getFrame(bufferedImage).image;
        return frame;
    }

    /**
     * 图片添加文本
     *
     * @param bufImg
     * @return
     */
    private  BufferedImage addSubtitle(BufferedImage bufImg) {

        // 添加字幕时的时间
        Font font = new Font("微软雅黑", Font.BOLD, 32);
        String timeContent = sdf.format(new Date());
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        Graphics2D graphics = bufImg.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        //设置图片背景
        graphics.drawImage(bufImg, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);

        //设置左上方时间显示
        graphics.setColor(Color.orange);
        graphics.setFont(font);
        graphics.drawString(timeContent, 0, metrics.getAscent());

        return bufImg;
    }
}
