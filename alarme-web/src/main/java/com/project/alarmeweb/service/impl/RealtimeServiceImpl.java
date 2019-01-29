package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Realtime;
import com.project.alarmeweb.mapper.RealtimeMapper;
import com.project.alarmeweb.service.RealtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealtimeServiceImpl implements RealtimeService {

    @Autowired private RealtimeMapper realtimeMapper;

    @Override
    public int insertRealtimeSchdl(Realtime realtime) {
        return realtimeMapper.insertRealtimeSchdl(realtime);
    }

    @Override
    public List<Realtime> selectRealtimeMasterList() {
        return realtimeMapper.selectRealtimeMasterList();
    }

    @Override
    public int updateActiveYn(Realtime realtime) { return realtimeMapper.updateActiveYn(realtime); }
}
