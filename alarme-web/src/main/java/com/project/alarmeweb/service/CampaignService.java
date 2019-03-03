package com.project.alarmeweb.service;

import java.util.List;
import java.util.Map;
import com.project.alarmeweb.dto.AddressGrp;
import com.project.alarmeweb.dto.Campaign;

public interface CampaignService {
	
	Campaign getCurrentDBDatetime();
    
	List<Campaign> selectCampaignList(Map<String, Object> params);
	
	Campaign selectCampaignInfo(int schdlId);
	
	List<AddressGrp> selectAddressGrpList(String loginId);
	
	int insertCampaignSchdl(Campaign campaign);
	
	int updateCampaignSchdl(Campaign campaign);
    
    int updateSchdlFilePath(Campaign campaign);
    
    int updateTargetFilePath(Campaign campaign);
    
    int updateSchdlSendFlag(Campaign campaign);

    int updateTargetStatus(Campaign campaign);

    Campaign selectTargetStatus(int schdlId);

    int deleteCampaign(int schdlId);
	
}
