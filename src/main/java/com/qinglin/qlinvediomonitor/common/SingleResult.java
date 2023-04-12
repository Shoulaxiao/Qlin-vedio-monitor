package com.qinglin.qlinvediomonitor.common;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SingleResult
 * @Description
 * @date 2023/4/11 10:02
 */
public class SingleResult<T> extends BaseResult {


    private static final long serialVersionUID = 4101179590769575819L;

    /**
     * id
     */
    private T data;

    /**
     *
     */
    public SingleResult() {
        super();
    }

    /**
     * @param data
     */
    public SingleResult(T data) {
        super();
        this.data = data;
    }

    /**
     * @param errorCode
     * @param errorMessage
     */
    public SingleResult(T data, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.data = data;
    }

    /**
     * @param success
     * @param errorCode
     * @param errorMessage
     */
    public SingleResult(T data, boolean success, String errorCode, String errorMessage) {
        super(success, errorCode, errorMessage);
        this.data = data;
    }

    /**
     * Getter for data
     *
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * Setter for data
     *
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String str = super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(",").append(data);
        return sb.toString();
    }
}
