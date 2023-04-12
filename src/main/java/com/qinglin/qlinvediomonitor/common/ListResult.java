package com.qinglin.qlinvediomonitor.common;

import java.util.Collection;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname ListResult
 * @Description
 * @date 2023/4/11 10:06
 */
public class ListResult <T> extends BaseResult{
    private static final long serialVersionUID = 8790322486199505772L;
    private Collection<T> values;

    /**
     * 无参构造函数
     */
    public ListResult() {
        super();
    }

    /**
     * 无参构造函数
     */
    public ListResult(Collection<T> values) {
        super();
        this.values = values;
    }

    /**
     * @param errorCode
     * @param errorMessage
     */
    public ListResult(Collection<T> values, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.values = values;
    }

    /**
     * @param success
     * @param errorCode
     * @param errorMessage
     */
    public ListResult(Collection<T> values, boolean success, String errorCode, String errorMessage) {
        super(success, errorCode, errorMessage);
        this.values = values;
    }

    /**
     * Getter for data
     *
     * @return
     */
    public Collection<T> getValues() {
        return values;
    }

    /**
     * Setter for data
     *
     * @param values
     */
    public void setValues(Collection<T> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        String str = super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(",");
        sb.append(values);
        return sb.toString();
    }
}
