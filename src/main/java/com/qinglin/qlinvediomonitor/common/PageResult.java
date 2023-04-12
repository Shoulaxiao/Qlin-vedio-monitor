package com.qinglin.qlinvediomonitor.common;

import java.util.Collection;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname PageResult
 * @Description
 * @date 2023/4/11 10:07
 */
public class PageResult<T> extends ListResult<T> {
    private static final long serialVersionUID = -4799503017629907735L;


    private PageInfo pageInfo;
    /**
     * 无参构造函数
     */
    public PageResult() {
        super();
    }

    /**
     * @param values
     * @param success
     * @param errorCode
     * @param errorMessage
     */
    public PageResult(Collection<T> values, boolean success, String errorCode, String errorMessage) {
        super(values, success, errorCode, errorMessage);
    }

    /**
     * @param values
     * @param errorCode
     * @param errorMessage
     */
    public PageResult(Collection<T> values, String errorCode, String errorMessage) {
        super(values, errorCode, errorMessage);
    }

    /**
     * @param values
     */
    public PageResult(Collection<T> values) {
        super(values);
    }

    /**
     * @param values
     */
    public PageResult(Collection<T> values, int page, int itemsPerPage, int items) {
        super(values);
        this.pageInfo = new PageInfo(items,itemsPerPage,page);
        this.pageInfo.setItems(items);
        this.pageInfo.setItemsPerPage(itemsPerPage);
        this.pageInfo.setPage(page);
    }

    /**
     * Getter method for property <tt>pageInfo</tt>.
     *
     * @return property value of pageInfo
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * Setter method for property <tt>pageInfo</tt>.
     *
     * @param pageInfo value to be assigned to property pageInfo
     */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        String str = super.toString();
        String pageStr = "";
        if (pageInfo != null) {
            pageStr = pageInfo.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(",").append(pageStr);
        return sb.toString();
    }

}
