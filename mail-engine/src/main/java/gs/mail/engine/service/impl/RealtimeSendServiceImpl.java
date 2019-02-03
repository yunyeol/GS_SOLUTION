package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.service.RealtimeSendService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RealtimeSendServiceImpl implements RealtimeSendService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    private ConcurrentHashMap<Long, Boolean> realtimeHashMap = new ConcurrentHashMap<Long, Boolean>();

    @Override
    public List<Realtime> selectRealtimeSchdlList() {
        List<Realtime> selectRealtimeSendList = sqlSessionTemplate.selectList("SQL.RealitmeSend.selectRealtimeSchdl");
        return selectRealtimeSendList;
    }

    @Override
    public boolean isRunningChk(long schdlId) {
        Boolean isRun = realtimeHashMap.get(schdlId);

        if(isRun == null){
            return  false;
        }else{
            return isRun;
        }
    }

    @Override
    public void setRunnging(long schdlId, boolean isRun) {
        if(isRun){
            realtimeHashMap.put(schdlId, isRun);
        }else{
            realtimeHashMap.remove(schdlId);
        }
    }
}
