package com.qinglin.qlinvediomonitor.exception;

/**
 * @author shoulaxiao
 * @version 1.0.0
 * @ClassName SmartBiErrorFactory.java
 * @Description
 * @createTime 2021年06月15日 16:38:00
 */
public class MonitorVideoErrorFactory extends AbstractErrorFactory {

    private static final MonitorVideoErrorFactory INSTANCE = new MonitorVideoErrorFactory();

    @Override
    protected String provideErrorBundleName() {
        return "sm";
    }

    public static MonitorVideoErrorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 私有构造
     */
    private MonitorVideoErrorFactory() {
    }

    public final ErrorInfo SYSTEM_BUSY = new ErrorInfo("访问错误", "AT0112DV0001");
    public final ErrorInfo VIDEO_NOT_FOUND = new ErrorInfo("视频未找到", "AT0112DV0002");
    public final ErrorInfo SAVE_MEDIA_FAIL = new ErrorInfo("保存失败", "AT0112DV0003");


    private ErrorInfo createCustomError(String errorCode, String args) {
        Message msg = getMessage(errorCode, args);
        LYError error = createError(msg.getMessage(), errorCode, args);
        return new ErrorInfo(error.getCode(), error.getMessage());
    }

    /**
     * 请求url失败
     */
    public ErrorInfo callUrlError(String message) {
        return createCustomError("AT0112DV0008", message);
    }

}
