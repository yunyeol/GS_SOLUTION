package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.User;
import com.project.alarmeweb.mapper.UserMapper;
import com.project.alarmeweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getMemberList(String loginId, String orderParam) {
        return userMapper.getMemberList(loginId, orderParam);
    }

    @Override
    public int deleteUsers(User user) {
        return userMapper.deleteUsers(user);
    }

    @Override
    public int updateActiveUsers(User user) {
        return userMapper.updateActiveUsers(user);
    }

    @Override
    public List<User> getAuthList() {
        return userMapper.getAuthList();
    }

    @Override
    public List<User> getGrpList() {
        return userMapper.getGrpList();
    }

    @Override
    public int insertUsers(User user) {
        return userMapper.insertUsers(user);
    }

    @Override
    public int updateAuthUsers(User user) {
        return userMapper.updateAuthUsers(user);
    }

    @Override
    public int updateGrpUsers(User user) {
        return userMapper.updateGrpUsers(user);
    }

    @Override
    public int updatePwdUsers(User user) {
        return userMapper.updatePwdUsers(user);
    }
}
