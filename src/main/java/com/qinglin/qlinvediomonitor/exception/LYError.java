package com.qinglin.qlinvediomonitor.exception;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname LYError
 * @Description
 * @date 2023/4/11 16:25
 */
public class LYError extends Message{

    /**  */
    private static final long  serialVersionUID   = 1L;

    /** 默认错误消息, 应用程序切勿使用该错误信息，只有SOF容器使用该错误信息 */
    public static final String DEFAULT_ERROR_MSG  = "SOF容器系统错误";

    /** 默认错误码, 应用程序切勿使用该错误码，只有SOF容器使用该错误码*/
    public static final String DEFAULT_ERROR_CODE = "LY0700100000";

    /** 错误码 */
    private String             code;

    /**
     * @param message 错误文案模板
     * @param messageKey 错误码
     * @param args 错误文案模板占位符参数
     */
    protected LYError(String errMsg, String code, Object... args) {
        super(errMsg, code, args);
        this.code = code;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LYError [code=" + code + ", err=" + getMessage() + "]";
    }
}
