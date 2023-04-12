package com.qinglin.qlinvediomonitor.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname AbstractMessageFactory
 * @Description
 * @date 2023/4/11 16:26
 */
public abstract class AbstractMessageFactory  extends MessageFactory{
    /**
     * 获取资源文件名称，资源文件名称是不包含[messages]后缀，和文件扩展类型
     * 通过提供的资源文件名称，消息工厂计算出资源文件完整路径
     *
     * 资源文件路径：
     * <code>META-INF/messages/bundleName-messages.properties</code>
     *
     * @return 返回资源文件名称
     */
    protected abstract String provideBundleName();

    /**
     * Factory method to create a new {@link Message} instance that is filled with the formatted
     * message with id <code>key</code> from xyz-messages.properties.
     * @param key Message key
     * @param args The parameters for formating message
     * @return Message
     */
    public final Message getMessage(String key, Object... args) {
        String bundleName = provideBundleName();
        if (StringUtils.isBlank(bundleName)) {
            throw new IllegalArgumentException("The name of the resource bundle is required");
        }
        return createMessage(getBundlePath(bundleName), key, args);
    }
}
