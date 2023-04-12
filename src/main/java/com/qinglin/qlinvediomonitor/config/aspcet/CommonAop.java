package com.qinglin.qlinvediomonitor.config.aspcet;

import com.alibaba.fastjson.JSON;
import com.qinglin.qlinvediomonitor.common.BasePageReq;
import com.qinglin.qlinvediomonitor.common.SingleResult;
import com.qinglin.qlinvediomonitor.exception.MonitorException;
import com.qinglin.qlinvediomonitor.exception.MonitorVideoErrorFactory;
import com.qinglin.qlinvediomonitor.utils.ErrorUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.UUID;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname CommonAop
 * @Description
 * @date 2023/4/11 16:32
 */
@Aspect
@Component
public class CommonAop {
    private static final Logger log = LoggerFactory.getLogger(CommonAop.class);

    @Around("execution(public * com.qinglin.qlinvediomonitor.controller..*.*(..))")
    public Object commonAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();
        try {
            String reqParam = setLogContext(proceedingJoinPoint);
            log.info("【调用开始】，request：{}", reqParam);
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            SingleResult singleResult = new SingleResult();
            if (e instanceof MonitorException) {
                MonitorException smartBiException = (MonitorException) e;
                log.error( "系统异常,errorCode:{},errorMsg:{}", e, smartBiException.getErrorCode(), smartBiException.getErrorMsg());
                ErrorUtil.buildSmartBiErrorInfo(singleResult, smartBiException);
            } else {
                log.error( "未知异常", e);
                ErrorUtil.buildErrorInfo(singleResult, MonitorVideoErrorFactory.getInstance().SYSTEM_BUSY);
            }
            result = singleResult;
        } finally {
            long end = System.currentTimeMillis();
            log.info( "【调用结束】，耗时：{}ms, response：{}", (end - start), JSON.toJSONString(result));
        }
        return result;
    }

    /**
     * 日志上下文设置
     *
     * @param proceedingJoinPoint
     *
     * @return
     */
    private String setLogContext(ProceedingJoinPoint proceedingJoinPoint) {

        StringBuilder params = new StringBuilder();
        Object[] args = proceedingJoinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest) {
                    params.append("ServletRequest;");
                    continue;
                }
                if (args[i] instanceof ServletResponse) {
                    params.append("ServletResponse;");
                    continue;
                }
                if (args[i] instanceof MultipartFile) {
                    params.append("MultipartFile;");
                    continue;
                }
                params.append(JSON.toJSONString(args[i]));
            }
        }
        return params.toString();
    }
}
