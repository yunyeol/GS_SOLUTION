package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Realtime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RealtimeMapper {
    int insertRealtimeSchdl(@Param("realtime") Realtime realtime);
    List<Realtime> selectRealtimeMasterList();
    int updateActiveYn(@Param("realtime") Realtime realtime);
}
