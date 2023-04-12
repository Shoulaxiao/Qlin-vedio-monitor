package com.qinglin.qlinvediomonitor.common;

import java.io.Serializable;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname BaseResult
 * @Description
 * @date 2023/4/11 10:02
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 6489532990314654229L;

    /**服务是否执行成功*/
    private boolean           success          = false;

    /** 错误码 */
    private String            errorCode;

    /** 错误信息 */
    private String            errorMessage;

    /**
     * 默认构造函数
     */
    public BaseResult() {
        super();
        this.success = true;
    }

    /**
     * 带参数构造函数
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public BaseResult(String errorCode, String errorMessage) {
        this(false, errorCode, errorMessage);
    }

    /**
     * 带参数构造函数
     *
     * @param success 服务是否执行成功
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public BaseResult(boolean success, String errorCode, String errorMessage) {
        super();
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Getter method for property <tt>success</tt>.
     *
     * @return property value of success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter method for property <tt>success</tt>.
     *
     * @param success value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter for error code
     *
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Setter for error code
     *
     * @param errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Getter for error message
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter for error message
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(success).append(",");
        sb.append(errorCode).append(",");
        sb.append(errorMessage);
        return sb.toString();
    }
}
