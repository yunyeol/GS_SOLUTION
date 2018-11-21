package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    List<Group> getGroupList();
}
