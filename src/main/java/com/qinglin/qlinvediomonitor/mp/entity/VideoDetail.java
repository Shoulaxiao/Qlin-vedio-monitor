package com.qinglin.qlinvediomonitor.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author shoulaxiao
 * @since 2023-04-11
 */
public class VideoDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 0-画面变动；1-有人移动
     */
    private Integer type;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 描述信息
     */
    private String desciption;

    /**
     * 预览信息图
     */
    private String preViewImgUrl;

    /**
     * 播放地址
     */
    private String url;

    /**
     * 创建时间
     */
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getPreViewImgUrl() {
        return preViewImgUrl;
    }

    public void setPreViewImgUrl(String preViewImgUrl) {
        this.preViewImgUrl = preViewImgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "VideoDetail{" +
        "id=" + id +
        ", userId=" + userId +
        ", type=" + type +
        ", title=" + title +
        ", desciption=" + desciption +
        ", preViewImgUrl=" + preViewImgUrl +
        ", url=" + url +
        ", createTime=" + createTime +
        "}";
    }
}
