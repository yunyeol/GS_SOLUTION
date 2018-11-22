package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Realtime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RealtimeMapper {
    int InsertRealtimeSchdl(@Param("realtime") Realtime realtime);
}
