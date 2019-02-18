package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Target;
import gs.mail.engine.service.TargetService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TargetServiceImpl implements TargetService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    private ConcurrentHashMap<Long, Boolean> targetHashMap = new ConcurrentHashMap<Long, Boolean>();

    @Override
    public List<Target> selectTargetList() {
        List<Target> selectTargetList = sqlSessionTemplate.selectList("SQL.Target.selectTargetList");
        return selectTargetList;
    }

    @Override
    public boolean isRunningChk(long schdlId) {
        Boolean isRun = targetHashMap.get(schdlId);

        if(isRun == null){
            return  false;
        }else{
            return isRun;
        }
    }

    @Override
    public void setRunnging(long schdlId, boolean isRun) {
        if(isRun){
            targetHashMap.put(schdlId, isRun);
        }else{
            targetHashMap.remove(schdlId);
        }
    }
}
