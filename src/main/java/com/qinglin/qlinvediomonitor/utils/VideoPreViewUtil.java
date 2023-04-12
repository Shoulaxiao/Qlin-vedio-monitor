package com.qinglin.qlinvediomonitor.utils;

import com.qinglin.qlinvediomonitor.service.MediaManageService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname VideoPreViewUtil
 * @Description
 * @date 2023/4/11 17:59
 */
@Component
public class VideoPreViewUtil{

    @Resource
    private MediaManageService localMediaManageService;

    /**
     * 获取视频缩略图
     *
     * @param filePath：视频路径
     * @throws Exception
     */
    public String randomGrabberFFmpegVideoImage(String filePath) throws Exception {
        String targetFilePath = "";
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
        ff.start();
        //判断是否是竖屏小视频
        String rotate = ff.getVideoMetadata("rotate");
        int ffLength = ff.getLengthInFrames();
        Frame f;
        int i = 0;
        //截取图片第几帧
        int index = 3;
        while (i < ffLength) {
            f = ff.grabImage();
            if (i == index) {
                if (null != rotate && rotate.length() > 1) {
                    //获取缩略图
                    targetFilePath = doExecuteFrame(f, true);
                } else {
                    //获取缩略图
                    targetFilePath = doExecuteFrame(f, false);
                }
                break;
            }
            i++;
        }
        ff.stop();
        return targetFilePath;
    }

    /**
     * 截取缩略图,保存到本地
     *
     * @param f
     * @return
     * @throws Exception
     */
    public String doExecuteFrame(Frame f, boolean bool) throws Exception {
        if (null == f || null == f.image) {
            return "";
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        if (bool) {
            Image image = bi;
            bi = rotate(image, 90);//图片旋转90度
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", os);
        byte[] sdf = os.toByteArray();
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return localMediaManageService.upLoad(input);
    }

    /**
     * 图片旋转角度
     *
     * @param src   源图片
     * @param angel 角度
     * @return 目标图片
     */
    public BufferedImage rotate(Image src, int angel) {
        int srcWidth = src.getWidth(null);
        int srcHeight = src.getHeight(null);
        // calculate the new image size
        Rectangle rectDes = calcRotatedSize(new Rectangle(new Dimension(
                srcWidth, srcHeight)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rectDes.width, rectDes.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform(这里先平移、再旋转比较方便处理；绘图时会采用这些变化，绘图默认从画布的左上顶点开始绘画，源图片的左上顶点与画布左上顶点对齐，然后开始绘画，修改坐标原点后，绘画对应的画布起始点改变，起到平移的效果；然后旋转图片即可)
        //平移（原理修改坐标系原点，绘图起点变了，起到了平移的效果，如果作用于旋转，则为旋转中心点）
        g2.translate((rectDes.width - srcWidth) / 2, (rectDes.height - srcHeight) / 2);

        //旋转（原理transalte(dx,dy)->rotate(radians)->transalte(-dx,-dy);修改坐标系原点后，旋转90度，然后再还原坐标系原点为(0,0),但是整个坐标系已经旋转了相应的度数 ）
        g2.rotate(Math.toRadians(angel), srcWidth / 2, srcHeight / 2);
        g2.drawImage(src, null, null);
        return res;
    }

    /**
     * 计算转换后目标矩形的宽高
     *
     * @param src   源矩形
     * @param angel 角度
     * @return 目标矩形
     */
    private Rectangle calcRotatedSize(Rectangle src, int angel) {
        double cos = Math.abs(Math.cos(Math.toRadians(angel)));
        double sin = Math.abs(Math.sin(Math.toRadians(angel)));
        int desWidth = (int) (src.width * cos) + (int) (src.height * sin);
        int desHeight = (int) (src.height * cos) + (int) (src.width * sin);
        return new java.awt.Rectangle(new Dimension(desWidth, desHeight));
    }
}
