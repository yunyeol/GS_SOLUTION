package com.project.alarmeweb.mapper;


import com.project.alarmeweb.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	public User getMemberList(@Param("loginId") String loginId);
}
