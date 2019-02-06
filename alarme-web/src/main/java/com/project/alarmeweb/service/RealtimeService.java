package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Realtime;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RealtimeService {
    int insertRealtimeSchdl(Realtime realtime);
    List<Realtime> selectRealtimeMasterList(Map<String,Object> params);
    int updateActiveYn(Realtime realtime);
    int removeRealtime(Long schdlId);
    int updateRealtimeSetting(Realtime realtime);
}
