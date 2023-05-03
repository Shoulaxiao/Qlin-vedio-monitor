package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.mp.entity.VideoDetail;
import com.qinglin.qlinvediomonitor.mp.mapper.VideoDetailMapper;
import com.qinglin.qlinvediomonitor.stream.detect.DetectService;
import com.qinglin.qlinvediomonitor.stream.detect.impl.HaarCascadeDetectService;
import com.qinglin.qlinvediomonitor.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P;

/**
 * @author will
 * @email zq2599@gmail.com
 * @date 2021/11/28 19:26
 * @description 将摄像头数据存储为动态检测并保存为mp4文件的应用
 */
@Slf4j
public class RecordRtmpSaveMp4 extends AbstractVideoApplication {

    @Resource
    private VideoDetailMapper videoDetailMapper;

    /**
     * "E:\\temp\\202111\\28\\camera-"
     * + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
     * + ".mp4";
     */
    private static final String RECORD_FILE_PATH = SystemUtils.prefixPath();

    private VideoDetectHandler videoDetectHandler;
    /**
     * 帧录制器
     */
    protected FrameRecorder recorder;

    @Override
    protected void initOutput() throws Exception {
        // 实例化FFmpegFrameRecorder
        recorder = new FFmpegFrameRecorder(RECORD_FILE_PATH,
                getCameraImageWidth(),
                getCameraImageHeight());

        // 文件格式
        recorder.setFormat("mp4");
        // 帧率与抓取器一致
        recorder.setFrameRate(getFrameRate());
        // 编码格式
        recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
        // 编码器类型
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
        // 视频质量，0表示无损
        recorder.setVideoQuality(0);
        // 初始化
        recorder.start();
        initDetectHandler();
    }

    @Override
    protected void output(Frame frame) throws Exception {
        FrameResult detectedFrame = videoDetectHandler.detect(frame);
        // 存盘
        recorder.record(detectedFrame.getFrame());
        // 信息保存到数据库
        saveVideoInfoToDb();
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


    private void saveVideoInfoToDb() {
        VideoDetail detail = new VideoDetail();
        detail.setUserId(1);
        detail.setType(VideoTypeEnum.CHARACTER_MOVE.getCode());
        detail.setTitle(VideoTypeEnum.CHARACTER_MOVE.getDesc());
        detail.setDesciption("");
        detail.setPreViewImgUrl("");
        detail.setUrl("");
        videoDetailMapper.insert(detail);
    }

    private void initDetectHandler() throws Exception {
        // 检测服务的初始化操作
        videoDetectHandler = new VideoDetectHandler();
        HaarCascadeDetectService faceDetect = new HaarCascadeDetectService("haarcascade_frontalface_alt.xml");
        HaarCascadeDetectService bodyDetect = new HaarCascadeDetectService("haarcascade_upperbody.xml");
        videoDetectHandler.addHandler(faceDetect)
                .addHandler(bodyDetect);
    }
}