package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.PageMaker;
import com.project.alarmeweb.dto.Receiver;
import com.project.alarmeweb.mapper.ReceiverMapper;
import com.project.alarmeweb.service.ReceiverService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
    public PageMaker getReceiverDeatil(Long addrGrpId, int currIdx, String selected, String keyword) {
        PageMaker pageMaker = PageMaker.getInstance();
        Map<String, Object> params = new HashMap<>();
        params.put("addrGrpId", addrGrpId);
        if( !"-1".equals(selected) ){
            pageMaker.setPaging(currIdx, Integer.parseInt(selected));
            params.put("isPaging", "Y");
            params.put("startRow",pageMaker.getStartRow());
            params.put("limitRow",pageMaker.getLimitRow());
        }
        if(StringUtils.isNotEmpty(keyword)){
            params.put("keyword", keyword);
        }

        //All or isNotAll
        List<Receiver> receivDetailList = receiverMapper.getReceiverDeatil(params);

        if( !CollectionUtils.isEmpty(receivDetailList) ){
            if( !"-1".equals(selected) ){
                pageMaker.setTotCnt( receiverMapper.getReceiverDeatilCnt(params)  );
                pageMaker.setTotPage(true, Integer.parseInt(selected));
                pageMaker.setStartShowRow(currIdx, Integer.parseInt(selected));
                if( pageMaker.getTotPage() == currIdx ){
                    pageMaker.setEndShowRow(pageMaker.getTotCnt());
                }else{
                    pageMaker.setEndShowRow(currIdx, Integer.parseInt(selected));
                }
            }else{
                pageMaker.setShowPaging(0,0,-1,1,receivDetailList.size());
            }

            pageMaker.setContentList(receivDetailList);
        }else{
            pageMaker.setShowPaging(0,0,-1,0,0);
        }

        return pageMaker;
    }

    @Override
    public int addReceiverDetail(Receiver receiver) { return receiverMapper.insertReceiverDetail(receiver); }

    @Override
    public int modifyReceiverDetail(Receiver receiver) { return receiverMapper.updateReceiverDetail(receiver); }

    @Override
    public int removeReceiverDetail(Long addrMbrId) { return receiverMapper.deleteReceiverDetail(addrMbrId); }

    @Override
    public int getReceivDetCheckRowCnt(Receiver receiver) { return receiverMapper.getReceivDetCheckRowCnt(receiver); }
}
