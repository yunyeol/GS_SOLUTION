package com.project.alarmeweb.mapper;


import com.project.alarmeweb.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getMemberList(@Param("loginId") String loginId,
                             @Param("orderParam") String orderParam);
    int deleteUsers(@Param("user") User user);
    int updateActiveUsers(@Param("user") User user);
    List<User> getAuthList();
    List<User> getGrpList();
    int insertUsers(@Param("user") User user);
    int updateAuthUsers(@Param("user") User user);
    int updateGrpUsers(@Param("user") User user);
    int updatePwdUsers(@Param("user") User user);
}
