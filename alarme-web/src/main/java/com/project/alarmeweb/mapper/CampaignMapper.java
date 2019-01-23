package com.project.alarmeweb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.alarmeweb.dto.AddressGrp;
import com.project.alarmeweb.dto.Campaign;

@Mapper
public interface CampaignMapper {
	
	Campaign getCurrentDBDatetime();
	
    List<Campaign> selectCampaignList();
    
    Campaign selectCampaignInfo(@Param("schdlId") int schdlId);
    
    List<AddressGrp> selectAddressGrpList(@Param("loginId") String loginId);
    
    int insertCampaignSchdl(@Param("campaign") Campaign campaign);
    
    int updateCampaignSchdl(@Param("campaign") Campaign campaign);
    
    int updateSchdlFilePath(@Param("campaign") Campaign campaign);
    
    int updateTargetFilePath(@Param("campaign") Campaign campaign);
    
    int updateSchdlSendFlag(@Param("campaign") Campaign campaign);
    
}
