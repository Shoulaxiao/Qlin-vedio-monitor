package com.qinglin.qlinvediomonitor.mp.service.impl;

import com.qinglin.qlinvediomonitor.mp.entity.User;
import com.qinglin.qlinvediomonitor.mp.mapper.UserMapper;
import com.qinglin.qlinvediomonitor.mp.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shoulaxiao
 * @since 2023-04-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
