package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Receiver;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReceiverMapper {
    List<Receiver> getReceiverList();
}
