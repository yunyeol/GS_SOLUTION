package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.PageMaker;
import com.project.alarmeweb.dto.Receiver;
import com.project.alarmeweb.mapper.ReceiverMapper;
import com.project.alarmeweb.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiverServiceImpl implements ReceiverService {
    @Autowired private ReceiverMapper receiverMapper;

    @Override
    public int getReceivGrpNameCnt(Map<String, String> params) { return receiverMapper.getReceivGrpNameCnt(params.get("addrGrpName")); }

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
    public int removeReceiver(Long addrGrpId) { return receiverMapper.deleteReceiver(addrGrpId); }

    @Override
    public PageMaker getReceiverDeatil(Long addrGrpId, int currIdx) {

        PageMaker pageMaker = PageMaker.getInstance();
        pageMaker.setPaging(currIdx);

        Map params = new HashMap<String, Object>();
        params.put("isPaging", "Y");
        params.put("startRow",pageMaker.getStartRow());
        params.put("endRow",pageMaker.getEndRow());

        List<Receiver> receivDetailList = receiverMapper.getReceiverDeatil(addrGrpId);

        pageMaker.setContentList( !CollectionUtils.isEmpty(receivDetailList) ? receivDetailList : new ArrayList<Receiver>() );

        return pageMaker;
    }

    @Override
    public int addReceiverDetail(Receiver receiver) { return receiverMapper.insertReceiverDetail(receiver); }

    @Override
    public int modifyReceiverDetail(Receiver receiver) { return receiverMapper.updateReceiverDetail(receiver); }

    @Override
    public int removeReceiverDetail(Long addrMbrId) { return receiverMapper.deleteReceiverDetail(addrMbrId); }
}
