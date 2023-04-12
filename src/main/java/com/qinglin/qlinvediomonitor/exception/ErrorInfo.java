/**
 * LY.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.qinglin.qlinvediomonitor.exception;

/**
 * 错误信息
 *
 * @author pyd48526
 * @version Id: ErrorInfo, v 0.1 2019/2/16 11:16 pyd48526 Exp $
 */
public final class ErrorInfo {
    private String errorCode;
    private String errorMsg;

    protected ErrorInfo(String errorMsg, String errorCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
