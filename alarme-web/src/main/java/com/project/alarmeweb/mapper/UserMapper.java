package com.project.alarmeweb.mapper;


import com.project.alarmeweb.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getMemberList(@Param("loginId") String loginId,
                             @Param("orderParam") String orderParam);
}
