package com.qinglin.qlinvediomonitor.service.impl;

import com.qinglin.qlinvediomonitor.exception.MonitorException;
import com.qinglin.qlinvediomonitor.exception.MonitorVideoErrorFactory;
import com.qinglin.qlinvediomonitor.model.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MediaManageServiceImpl
 * @Description
 * @date 2023/4/11 18:15
 */
@Service("LocalMediaManageService")
@Slf4j
public class LocalMediaManageServiceImpl extends AbstractMediaManage {

    @Override
    public String upLoad(InputStream input) throws MonitorException {
        String url = null;
        try {
            MultipartFile file = new MockMultipartFile("temp.jpg", "temp.jpg", "temp.jpg", input);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String realPath = request.getSession().getServletContext().getRealPath(Constant.PICTURE_PATH);
            File filePath = new File(realPath);
            if (!filePath.exists() && !filePath.isDirectory()) {
                boolean mkdirs = filePath.mkdirs();
                System.out.println(mkdirs);
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            String fileName = uuid + "-" + file.getOriginalFilename();
            file.transferTo(new File(realPath, fileName));
            url = Constant.PICTURE_PATH + "/" + fileName;
        } catch (IOException e) {
            log.error("保存到本地失败", e);
            throw new MonitorException(MonitorVideoErrorFactory.getInstance().SAVE_MEDIA_FAIL);
        }
        log.info("保存后的url={}", url);
        return url;
    }
}
