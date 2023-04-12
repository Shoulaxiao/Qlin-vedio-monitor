package com.qinglin.qlinvediomonitor.exception;

/**
 * @author shoulaxiao
 * @version 1.0.0
 * @ClassName SmartBiException.java
 * @Description SmartBiException项目异常
 * @createTime 2021年06月15日 16:34:00
 */
public class MonitorException extends Exception{

    private static final long serialVersionUID = -5022446493288634932L;

    private ErrorInfo errorInfo;

    public MonitorException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMsg());
        this.errorInfo = errorInfo;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
    public String getErrorCode() {
        return errorInfo.getErrorCode();
    }

    public String getErrorMsg(){
        return errorInfo.getErrorMsg();
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

}
