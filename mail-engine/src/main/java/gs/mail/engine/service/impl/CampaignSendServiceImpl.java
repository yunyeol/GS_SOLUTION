package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Campaign;
import gs.mail.engine.service.CampaignSendService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CampaignSendServiceImpl implements CampaignSendService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    private ConcurrentHashMap<Long, Boolean> campaignHashMap = new ConcurrentHashMap<Long, Boolean>();

    @Override
    public List<Campaign> selectCampaignSchdlList() {
        List<Campaign> selectCampaignSendList = sqlSessionTemplate.selectList("SQL.CampaignSend.selectCampaignSchdl");
        return selectCampaignSendList;
    }

    @Override
    public boolean isRunningChk(long schdlId) {
        Boolean isRun = campaignHashMap.get(schdlId);

        if(isRun == null){
            return  false;
        }else{
            return isRun;
        }
    }

    @Override
    public void setRunnging(long schdlId, boolean isRun) {
        if(isRun){
            campaignHashMap.put(schdlId, isRun);
        }else{
            campaignHashMap.remove(schdlId);
        }
    }
}
