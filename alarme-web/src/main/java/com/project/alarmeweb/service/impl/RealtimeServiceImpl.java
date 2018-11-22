package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Realtime;
import com.project.alarmeweb.mapper.RealtimeMapper;
import com.project.alarmeweb.service.RealtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealtimeServiceImpl implements RealtimeService {

    @Autowired private RealtimeMapper realtimeMapper;

    @Override
    public int insertRealtimeSchdl(Realtime realtime) {
        return realtimeMapper.InsertRealtimeSchdl(realtime);
    }
}
