package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Receiver;

import java.util.List;
import java.util.Map;

public interface ReceiverService {
    List<Receiver> getReceiverList();
    int getReceivGrpNameCnt(Map<String,Object> params);
    int addReceiver(Receiver receiver);
    int modifyReceiver(Receiver receiver);
    int removeReceiver(Long addrGrpId);
}
