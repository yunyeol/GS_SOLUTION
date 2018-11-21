package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Group;
import com.project.alarmeweb.mapper.GroupMapper;
import com.project.alarmeweb.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired private GroupMapper groupMapper;

    @Override
    public List<Group> getGroupList() {
        return groupMapper.getGroupList();
    }
}
