package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Realtime;

import java.util.List;

public interface RealtimeService {
    int insertRealtimeSchdl(Realtime realtime);
    List<Realtime> selectRealtimeMasterList();
}
