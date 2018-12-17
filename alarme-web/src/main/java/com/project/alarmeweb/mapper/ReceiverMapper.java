package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Receiver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReceiverMapper {
    List<Receiver> getReceiverList();
    int getReceivGrpNameCnt(@Param("addrGrpName") String addrGrpName);
    int insertReceiver(Receiver receiver);
    int updateReceiver(Receiver receiver);
    int deleteReceiver(@Param("addrGrpId") Long addrGrpId);
}
