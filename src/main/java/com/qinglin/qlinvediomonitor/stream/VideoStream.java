package com.qinglin.qlinvediomonitor.stream;

public interface VideoStream {

    /**
     * 推流
     * @param source
     * @param targetAddress
     */
    public void pushStream(String source,String targetAddress);

}
