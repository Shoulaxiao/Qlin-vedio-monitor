/**
 * LY.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.qinglin.qlinvediomonitor.utils;


import com.qinglin.qlinvediomonitor.common.SingleResult;
import com.qinglin.qlinvediomonitor.exception.ErrorInfo;
import com.qinglin.qlinvediomonitor.exception.MonitorException;

/**
 * @author pyd48526
 * @version Id: ErrorUtil, v 0.1 2019/1/21 14:18 pyd48526 Exp $
 */
public class ErrorUtil {

    public static void buildSmartBiErrorInfo(SingleResult singleResult, MonitorException exception) {
        singleResult.setSuccess(false);
        singleResult.setErrorCode(exception.getErrorCode());
        singleResult.setErrorMessage(exception.getErrorMsg());
    }

    public static void buildErrorInfo(SingleResult singleResult, ErrorInfo error) {
        singleResult.setSuccess(false);
        singleResult.setErrorCode(error.getErrorCode());
        singleResult.setErrorMessage(error.getErrorMsg());
    }
}
