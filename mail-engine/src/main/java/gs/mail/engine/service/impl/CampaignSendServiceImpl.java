package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Campaign;
import gs.mail.engine.service.CampaignSendService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CampaignSendServiceImpl implements CampaignSendService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<Campaign> selectCampaignSchdlList() {
        List<Campaign> selectCampaignSendList = sqlSessionTemplate.selectList("SQL.CampaignSend.selectCampaignSchdl");
        return selectCampaignSendList;
    }
}
