package gs.mail.engine.service;

import gs.mail.engine.dto.Campaign;

import java.util.List;

public interface CampaignSendService {
    List<Campaign> selectCampaignSchdlList();
}
