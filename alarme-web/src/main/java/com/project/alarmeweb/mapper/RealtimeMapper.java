package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Realtime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RealtimeMapper {
    int insertRealtimeSchdl(@Param("realtime") Realtime realtime);
    List<Realtime> selectRealtimeMasterList(Map<String,Object> params);
    int selectRealtimeMasterListCnt(Map<String,Object> params);
    int updateActiveYn(@Param("realtime") Realtime realtime);
    int removeRealtime(@Param("schdlId") Long schdlId);
    int updateRealtimeSetting(@Param("realtime") Realtime realtime);
}
