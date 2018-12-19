package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.PageMaker;
import com.project.alarmeweb.dto.Receiver;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ReceiverService {
    List<Receiver> getReceiverList();
    int getReceivGrpNameCnt(Map<String,String> params);
    int addReceiver(Receiver receiver);
    int modifyReceiver(Receiver receiver);
    int removeReceiver(Long addrGrpId);
    PageMaker getReceiverDeatil(Long addrGrpId, int currIdx);
    int addReceiverDetail(Receiver receiver);
    int modifyReceiverDetail(Receiver receiver);
    int removeReceiverDetail(@Param("addrMbrId") Long addrMbrId);
}
