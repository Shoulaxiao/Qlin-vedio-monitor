package com.qinglin.qlinvediomonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinglin.qlinvediomonitor.common.PageInfo;
import com.qinglin.qlinvediomonitor.common.PageResult;
import com.qinglin.qlinvediomonitor.exception.MonitorException;
import com.qinglin.qlinvediomonitor.exception.MonitorVideoErrorFactory;
import com.qinglin.qlinvediomonitor.model.VideoDto;
import com.qinglin.qlinvediomonitor.model.VideoReq;
import com.qinglin.qlinvediomonitor.mp.entity.VideoDetail;
import com.qinglin.qlinvediomonitor.mp.mapper.VideoDetailMapper;
import com.qinglin.qlinvediomonitor.service.MonitorVideoService;
import com.qinglin.qlinvediomonitor.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname MonitorVideoServiceImpl
 * @Description
 * @date 2023/4/11 15:02
 */
@Service
public class MonitorVideoServiceImpl implements MonitorVideoService {

    @Resource
    private VideoDetailMapper videoDetailMapper;

    @Override
    public PageResult<VideoDto> queryPage(VideoReq req) {
        LambdaQueryWrapper<VideoDetail> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.ge(VideoDetail::getCreateTime, DateUtils.getDayBegin(req.getDate()))
                .le(VideoDetail::getCreateTime, DateUtils.getEndOfDay(req.getDate()))
                .orderByDesc(VideoDetail::getCreateTime);
        Page<VideoDetail> videoDetailPage = videoDetailMapper.selectPage(new Page<>(req.getPage(), req.getPageSize()), queryWrapper);
        PageResult<VideoDto> result = new PageResult<>();
        if (CollectionUtils.isNotEmpty(videoDetailPage.getRecords())) {
            List<VideoDto> data = videoDetailPage.getRecords().stream().map(this::do2dto).collect(Collectors.toList());
            result.setValues(data);
        }

        PageInfo pageInfo = new PageInfo((int) videoDetailPage.getTotal(),
                (int) videoDetailPage.getSize(),
                (int) videoDetailPage.getCurrent());
        result.setPageInfo(pageInfo);
        result.setSuccess(true);
        return result;
    }

    @Override
    public String getPlayerUrl(Long id) throws MonitorException {
        VideoDetail videoDetail = videoDetailMapper.selectById(id);
        if (videoDetail == null) {
            throw new MonitorException(MonitorVideoErrorFactory.getInstance().VIDEO_NOT_FOUND);
        }
        return videoDetail.getUrl();
    }

    @Override
    public VideoDto getPlayerDetail(Long id) throws MonitorException {
        VideoDetail videoDetail = videoDetailMapper.selectById(id);
        if (videoDetail == null) {
            throw new MonitorException(MonitorVideoErrorFactory.getInstance().VIDEO_NOT_FOUND);
        }
        return do2dto(videoDetail);
    }


    private VideoDto do2dto(VideoDetail videoDetail){
        VideoDto videoDto = new VideoDto();
        videoDto.setId(videoDetail.getId());
        videoDto.setDescription(videoDetail.getDesciption());
        videoDto.setTitle(videoDetail.getTitle());
        videoDto.setUrl(videoDetail.getUrl());
        videoDto.setType(videoDetail.getType());
        videoDto.setPreViewImgUrl(videoDetail.getPreViewImgUrl());
        videoDto.setCreateTime(videoDetail.getCreateTime());
        return videoDto;
    }
}
