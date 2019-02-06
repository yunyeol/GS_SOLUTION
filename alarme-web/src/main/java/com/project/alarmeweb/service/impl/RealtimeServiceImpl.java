package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Realtime;
import com.project.alarmeweb.mapper.RealtimeMapper;
import com.project.alarmeweb.service.RealtimeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RealtimeServiceImpl implements RealtimeService {

    @Value("${html.header}") private String htmlHeader;
    @Value("${html.footer}") private String htmlFooter;

    @Autowired private RealtimeMapper realtimeMapper;

    @Override
    public int insertRealtimeSchdl(Realtime realtime) {
        return realtimeMapper.insertRealtimeSchdl(realtime);
    }

    @Override
    public List<Realtime> selectRealtimeMasterList(Map<String,Object> params) {
        return realtimeMapper.selectRealtimeMasterList(params);
    }

    @Override
    public int updateActiveYn(Realtime realtime) { return realtimeMapper.updateActiveYn(realtime); }

    @Override
    public int removeRealtime(Long schdlId) {
        int resultCnt = 0;
        Map<String,Object> params = new HashMap<>();
        params.put("schdlId",schdlId);
        List<Realtime> realTimeList = selectRealtimeMasterList(params);

        if(  !CollectionUtils.isEmpty(realTimeList) ){
            String filePath = StringUtils.isNotEmpty(realTimeList.get(0).getFilePath()) ? realTimeList.get(0).getFilePath() : "";
            File file = new File(filePath);
            if(file.isFile()){
                file.delete();
            }
            resultCnt = realtimeMapper.removeRealtime(schdlId);
        }
        return resultCnt;
    }

    @Override
    public int updateRealtimeSetting(Realtime realtime) {
        Map<String,Object> params = new HashMap<>();
        params.put("schdlId",realtime.getSchdlId());
        List<Realtime> realTimeList = selectRealtimeMasterList(params);

        if(  !CollectionUtils.isEmpty(realTimeList) ){
            String filePath = StringUtils.isNotEmpty(realTimeList.get(0).getFilePath()) ? realTimeList.get(0).getFilePath() : "";
            try{
                File file = new File(filePath);
                if ( file.isFile() ){
                    BufferedWriter bfw = new BufferedWriter(new FileWriter(file));
                    bfw.write(htmlHeader+realtime.getFilePathHtml()+htmlFooter);
                    bfw.flush();
                    bfw.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return realtimeMapper.updateRealtimeSetting(realtime);
    }
}
