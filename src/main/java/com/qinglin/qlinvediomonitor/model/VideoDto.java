package com.qinglin.qlinvediomonitor.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname VideoDto
 * @Description
 * @date 2023/4/11 10:08
 */
@Data
public class VideoDto implements Serializable {
    private static final long serialVersionUID = 7761172604959362035L;


    private long id;

    private String url;

    private int type;

    private String preViewImgUrl;

    private Date createTime;

    private String title;

    private String description;
}
