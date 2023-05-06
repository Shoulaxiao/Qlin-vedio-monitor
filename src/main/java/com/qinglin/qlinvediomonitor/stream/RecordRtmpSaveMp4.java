package com.qinglin.qlinvediomonitor.stream;

import com.qinglin.qlinvediomonitor.enums.VideoTypeEnum;
import com.qinglin.qlinvediomonitor.model.FrameResult;
import com.qinglin.qlinvediomonitor.mp.entity.VideoDetail;
import com.qinglin.qlinvediomonitor.mp.mapper.VideoDetailMapper;
import com.qinglin.qlinvediomonitor.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.annotation.Resource;

import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_YUV420P;


/**
 * @author will
 * @email zq2599@gmail.com
 * @date 2021/11/28 19:26
 * @description 将摄像头数据存储为动态检测并保存为mp4文件的应用
 */
@Slf4j
public class RecordRtmpSaveMp4 extends AbstractVideoApplication {
    private final OpenCVFrameConverter.ToIplImage openCVConverter = new OpenCVFrameConverter.ToIplImage();

    @Resource
    private VideoDetailMapper videoDetailMapper;

    /**
     * "E:\\temp\\202111\\28\\camera-"
     * + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
     * + ".mp4";
     */
    private static final String RECORD_FILE_PATH = SystemUtils.prefixPath();

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
        // 存盘
        recorder.record(frame);
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

    @Override
    protected FrameResult frameDetect(Frame mat2Frame) {

        return null;
    }

    @Override
    protected opencv_core.IplImage frame2Img(Frame frame) {
        return openCVConverter.convert(frame);
    }

    @Override
    protected Frame mat2Frame(opencv_core.Mat mat) {
        return openCVConverter.convert(mat);
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


}