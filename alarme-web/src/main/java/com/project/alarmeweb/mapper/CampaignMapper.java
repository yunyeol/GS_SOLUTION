package com.project.alarmeweb.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.alarmeweb.dto.AddressGrp;
import com.project.alarmeweb.dto.Campaign;

@Mapper
public interface CampaignMapper {
	
	Campaign getCurrentDBDatetime();
	
    List<Campaign> selectCampaignList(Map<String, Object> params);
    
    Campaign selectCampaignInfo(@Param("schdlId") int schdlId);
    
    List<AddressGrp> selectAddressGrpList(@Param("loginId") String loginId);
    
    int insertCampaignSchdl(@Param("campaign") Campaign campaign);
    
    int updateCampaignSchdl(@Param("campaign") Campaign campaign);
    
    int updateSchdlFilePath(@Param("campaign") Campaign campaign);
    
    int updateTargetFilePath(@Param("campaign") Campaign campaign);
    
    int updateSchdlSendFlag(@Param("campaign") Campaign campaign);

    int updateTargetStatus(@Param("campaign") Campaign campaign);

    Campaign selectTargetStatus(@Param("schdlId") int schdlId);

    int deleteCampaign(@Param("schdlId") int schdlId);

}
