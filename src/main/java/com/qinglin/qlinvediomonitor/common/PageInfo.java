package com.qinglin.qlinvediomonitor.common;

import java.io.Serializable;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname PageInfo
 * @Description
 * @date 2023/4/11 10:05
 */
public class PageInfo implements Serializable {
    private static final long serialVersionUID = -6611755386506157318L;

    /** 总共项数 */
    private int               items;

    /** 每页项数，默认20 */
    private int               itemsPerPage     = 20;

    /** 当前页码，默认1 */
    private int               page             = 1;

    /**
     * 无参构造器
     */
    public PageInfo() {
        super();
    }

    /**
     * 带参构造器
     * @param itemsPerPage
     * @param page
     */
    public PageInfo(int itemsPerPage, int page) {
        super();
        this.itemsPerPage = itemsPerPage;
        this.page = page;
    }

    /**
     * 带参构造器
     * @param items
     * @param itemsPerPage
     * @param page
     */
    public PageInfo(int items, int itemsPerPage, int page) {
        super();
        this.items = items;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
    }

    /**
     * 取得总页数。
     *
     * @return 总页数
     */
    public int getPages() {
        return (int) Math.ceil((double) items / itemsPerPage);
    }

    /**
     * XFIRE需要set方法
     */
    public void setPages(int pages) {
        // no-op
    }

    // ~getters and setters ------------------------------------------------------------

    /**
     * @return
     */
    public int getItems() {
        return items;
    }

    /**
     * @param items
     */
    public void setItems(int items) {
        this.items = items;
    }

    /**
     * @return
     */
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    /**
     * @param itemsPerPage
     */
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * @return
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(page).append(",").append(itemsPerPage).append(",").append(items);
        return sb.toString();
    }
}
