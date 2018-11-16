package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.User;
import com.project.alarmeweb.mapper.UserMapper;
import com.project.alarmeweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getMemberList(String loginId) {
        return userMapper.getMemberList(loginId);
    }
}
