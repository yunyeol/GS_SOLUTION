package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Receiver;
import com.project.alarmeweb.mapper.ReceiverMapper;
import com.project.alarmeweb.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiverServiceImpl implements ReceiverService {
    @Autowired private ReceiverMapper receiverMapper;

    @Override
    public List<Receiver> getReceiverList() {
        return receiverMapper.getReceiverList();
    }

    @Override
    public int addReceiver(Receiver receiver) {
        return receiverMapper.insertReceiver(receiver);
    }

    @Override
    public int modifyReceiver(Receiver receiver) {
        return receiverMapper.updateReceiver(receiver);
    }

    @Override
    public int removeReceiver(Long id) {
        return receiverMapper.deleteReceiver(id);
    }
}