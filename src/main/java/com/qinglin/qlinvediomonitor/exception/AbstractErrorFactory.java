package com.qinglin.qlinvediomonitor.exception;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname AbstractErrorFactory
 * @Description
 * @date 2023/4/11 16:25
 */
public abstract class AbstractErrorFactory extends AbstractMessageFactory {
    private static final String BUNDLE_ERROR_SUFFIX = "-error";

    /**
     * 获取错误消息资源文件名称，资源文件名称是不包含[error-messages]后缀，和文件扩展类型
     * 通过提供的错误消息资源文件名称，消息工厂计算出资源文件完整路径
     *
     * 资源文件路径：
     * <code>META-INF/messages/bundleName-error-messages.properties</code>
     *
     * @return 错误消息资源文件名称
     */
    protected abstract String provideErrorBundleName();

    /**
     * @see com.ly.sof.model.i18n.AbstractMessageFactory#provideBundleName()
     */
    @Override
    protected final String provideBundleName() {
        return provideErrorBundleName() + BUNDLE_ERROR_SUFFIX;
    }

    /**
     * 创建错误消息对象
     *
     * @param message 错误描述
     * @param errorCode 错误代码
     * @return 错误消息
     */
    public static LYError createStaticError(String message, String errorCode) {
        return new LYError(message, errorCode);
    }

    /**
     * 从错误消息描述资源文件中根据错误代码创建错误消息对象
     *
     * @param errorCode 错误代码
     * @param args 错误消息占位符参数
     * @return 错误消息
     */
    protected final LYError createError(String errorCode, Object... args) {
        Message msg = getMessage(errorCode, args);
        LYError error = new LYError(msg.getMessage(), errorCode, args);
        return error;
    }
}
