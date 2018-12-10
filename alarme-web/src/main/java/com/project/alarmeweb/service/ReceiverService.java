package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Receiver;

import java.util.List;

public interface ReceiverService {
    List<Receiver> getReceiverList();
    int getReceivGrpNameCnt(String addrGrpName);
    int addReceiver(Receiver receiver);
    int modifyReceiver(Receiver receiver);
    int removeReceiver(Long id);
}
