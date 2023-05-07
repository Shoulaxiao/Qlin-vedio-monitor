package com.qinglin.qlinvediomonitor.controller;

import com.qinglin.qlinvediomonitor.common.PageResult;
import com.qinglin.qlinvediomonitor.common.SingleResult;
import com.qinglin.qlinvediomonitor.exception.MonitorException;
import com.qinglin.qlinvediomonitor.model.VideoDto;
import com.qinglin.qlinvediomonitor.model.VideoReq;
import com.qinglin.qlinvediomonitor.service.MonitorVideoService;
import com.qinglin.qlinvediomonitor.utils.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MonitorVideoController
 * @Description
 * @date 2023/4/10 18:10
 */
@RequestMapping(value = "/api")
@Controller
@Slf4j
public class MonitorVideoController {

    @Value("${http.pull.stream.address}")
    private String liveUrl;

    @Resource
    private MonitorVideoService monitorVideoService;


    @RequestMapping(value = "/video/getLiveUrl")
    @ResponseBody
    public SingleResult<String> getLiveUrl() {
        return new SingleResult<>(liveUrl, true, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @RequestMapping(value = "/video/queryVideosListPage", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<VideoDto> queryVideosListPage(@RequestBody VideoReq req) {
        PageResult<VideoDto> result = new PageResult<>();
        try {
            result = monitorVideoService.queryPage(req);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("查询运动检测视频异常", e);
            result.setSuccess(false);
        }
        return result;
    }


    @RequestMapping(value = "/video/queryDetailById", method = RequestMethod.GET)
    @ResponseBody
    public SingleResult<VideoDto> queryDetailById(Long id, HttpServletRequest request) throws MonitorException {
        SingleResult<VideoDto> singleResult = new SingleResult<VideoDto>();
        VideoDto playerDetail = monitorVideoService.getPlayerDetail(id);
        playerDetail.setUrl(UrlUtil.parseVideoPlayUrl(request, playerDetail.getId()));
        singleResult.setData(playerDetail);
        singleResult.setSuccess(true);
        return singleResult;
    }


    @RequestMapping(value = "/video/getVideo", method = RequestMethod.GET)
    @ResponseBody
    public void getVideo(Long id,
                         HttpServletRequest request, HttpServletResponse response) throws IOException, MonitorException {
        // 视频路径
        log.info("需要播放的视频id={}", id);
        String file = monitorVideoService.getPlayerUrl(id);
        FileInputStream inputStream = null;
        try {
            File file1 = new File(file);
            //	获得视频文件的输入流
            inputStream = new FileInputStream(file1);
            // 创建字节数组，数组大小为视频文件大小
            byte[] data = new byte[inputStream.available()];
            // 将视频文件读入到字节数组中
            inputStream.read(data);
            // 设置返回数据格式
            response.setContentType("video/mp4");
            response.setHeader("Content-Disposition", "attachment; filename=" + file1.getName().replace(" ", "_"));
            System.out.println("data.length " + data.length);
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + Integer.valueOf(data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", "" + file.length());
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (NoSuchFileException e) {
            log.error("播放异常", e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            log.error("播放异常", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
