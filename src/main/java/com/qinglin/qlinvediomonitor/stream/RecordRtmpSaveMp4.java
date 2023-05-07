package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.model.RecordEvent;
import com.qinglin.qlinvediomonitor.mp.entity.VideoDetail;
import com.qinglin.qlinvediomonitor.mp.mapper.VideoDetailMapper;
import com.qinglin.qlinvediomonitor.stream.detect.impl.VideoMoveDetectService;
import com.qinglin.qlinvediomonitor.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_YUV420P;


/**
 * @author will
 * @email zq2599@gmail.com
 * @date 2021/11/28 19:26
 * @description 将摄像头数据存储为动态检测并保存为mp4文件的应用
 */
@Slf4j
@Service(value = "recordRtmpSaveMp4")
public class RecordRtmpSaveMp4 extends AbstractVideoApplication implements RecordAction {
    private final OpenCVFrameConverter.ToIplImage openCVConverter = new OpenCVFrameConverter.ToIplImage();

    private final static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    @Resource
    private VideoDetailMapper videoDetailMapper;

    /**
     * "E:\\temp\\202111\\28\\camera-"
     * + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
     * + ".mp4";
     */
    private static final String RECORD_FILE_PATH = SystemUtils.prefixPath();

    /**
     * 帧间变化
     */
    private int FRAME_MOVE_COUNT = 5;


    private long index = 0;


    /**
     * 用来判断是否运动，用多次帧间变化来判断，避免出现误判，默认采用5次帧间
     */
    private final LinkedList<Boolean> isMove = new LinkedList<Boolean>();


    /**
     * 帧录制器
     */
    protected FrameRecorder recorder;


    private VideoDetectHandler moveDetectHandler;

    private String fileName;
    private String preViewUrl;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 前一帧
     */
    private Mat preFrame;


    private long startTime;

    @Override
    protected void initOutput() throws Exception {
        FRAME_MOVE_COUNT = (int) getFrameRate();
        this.initDetectHandler();
    }


    @Override
    protected void initDetectHandler() throws Exception {
        // 添加画面运动检测器
        this.moveDetectHandler = new VideoDetectHandler();
        this.moveDetectHandler.addHandler(new VideoMoveDetectService());
        // 调用父类初始化人脸检测器
        super.initDetectHandler();
    }

    @Override
    protected void output(FrameResult frame) throws Exception {

    }

    private void processRecordEnd(Frame frame) throws FrameRecorder.Exception {

        //文件储存对象
        recorder.stop();
        // 信息保存到数据库
        saveVideoInfoToDb(preViewUrl);
    }


    protected void writeToFile(Frame frame, String preViewUrl) {
        File targetFile = new File(preViewUrl);
        String imgSuffix = "png";

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        } catch (Exception e) {
            log.error("保存缩略图失败", e);
        }
    }

    private void processRecordStart(FrameResult frame) throws FrameRecorder.Exception {
        initRecorder();
        // 存盘
        recorder.record(frame.getFrame());
    }


    @Override
    protected void releaseOutputResource() throws Exception {
        recorder.close();
        // 检测工具也要释放资源
        videoDetectHandler.release();
    }

    @Override
    protected int getInterval() {
        return super.getInterval() / 8;
    }


    @Override
    protected FrameResult frameHandle(Frame frame, Mat mat) {
        index++;
        FrameResult result;

        Mat curFrame = Java2DFrameUtils.toMat(frame);
        result = moveDetectHandler.detect(null == preFrame ? curFrame : preFrame, curFrame);
        isMove.add(result.isSuccess());
        this.preFrame = curFrame;

        // 判断是否开始录制，5次都有变化
        if (isMove.size() >= FRAME_MOVE_COUNT) {
            result.setMove(isStartAction());
            // 判断是否结束录制
            result.setEnd(isEndAction());
        }
        result.setFrame(frame);

        if (index > FRAME_MOVE_COUNT) {
            // 去掉链表首部的
            isMove.remove(0);
        }
        eventPublisher.publishEvent(new RecordEvent(result));
        return result;
    }

    @Override
    protected IplImage frame2Img(Frame frame) {
        return openCVConverter.convert(frame);
    }

    private void initRecorder() throws FrameRecorder.Exception {
        // 实例化FFmpegFrameRecorder
        recorder = new FFmpegFrameRecorder(new File(RECORD_FILE_PATH + this.fileName + ".mp4"),
                getCameraImageWidth(),
                getCameraImageHeight());

        // 文件格式
        recorder.setFormat("mp4");

        // 帧率与抓取器一致
        recorder.setFrameRate(getFrameRate());
        // 编码器类型
        recorder.setVideoCodec(grabber.getVideoCodec());
        recorder.setGopSize((int) getFrameRate());

        // 视频质量，0表示无损
        recorder.setVideoQuality(0);
        // 初始化
        recorder.start();
    }

    @Override
    protected Frame mat2Frame(Mat mat) {
        return openCVConverter.convert(mat);
    }


    private void saveVideoInfoToDb(String preViewUrl) {
        VideoDetail detail = new VideoDetail();
        detail.setUserId(1);
        detail.setType(VideoTypeEnum.PICTURE_MOVE.getCode());
        detail.setTitle(VideoTypeEnum.PICTURE_MOVE.getDesc());
        detail.setDesciption("");
        detail.setPreViewImgUrl(preViewUrl);
        detail.setUrl(RECORD_FILE_PATH + this.fileName + ".mp4");
        videoDetailMapper.insert(detail);
    }

    private boolean isStartAction() {
        for (Boolean aBoolean : isMove) {
            if (!aBoolean) {
                return false;
            }
        }
        return true;
    }

    private boolean isEndAction() {
        for (Boolean aBoolean : isMove) {
            if (aBoolean) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void start(FrameResult frameResult) throws FrameRecorder.Exception {
        this.fileName = format.format(new Date());
        processRecordStart(frameResult);
        this.startTime = System.currentTimeMillis();
        log.info("===开始录制{}.mp4===", fileName);
        log.info("录制中...");
    }

    @Override
    public void end(FrameResult frameResult) throws FrameRecorder.Exception {
        processRecordEnd(frameResult.getFrame());
        long endTime = System.currentTimeMillis();
        this.preViewUrl = null;
        log.info("===结束录制{}.mp4，耗时=[{}]s===", fileName, (endTime - startTime) / 1000);
    }

    @Override
    public void process(FrameResult frameResult) throws FrameRecorder.Exception {
        if (StringUtils.isBlank(preViewUrl)) {
            // 保存缩略图
            String preViewUrl = SystemUtils.WEB_STATIC_IMG + fileName + ".png";
            String preViewUrlLocal = SystemUtils.WEB_STATIC_IMG_LOCAL + fileName + ".png";
            writeToFile(frameResult.getFrame(), preViewUrlLocal);
            this.preViewUrl = preViewUrl;
        }
        recorder.record(frameResult.getFrame());
    }
}