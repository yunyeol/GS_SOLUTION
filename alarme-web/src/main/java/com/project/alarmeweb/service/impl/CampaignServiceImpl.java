package com.project.alarmeweb.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.alarmeweb.dto.AddressGrp;
import com.project.alarmeweb.dto.Campaign;
import com.project.alarmeweb.mapper.CampaignMapper;
import com.project.alarmeweb.service.CampaignService;

@Service
public class CampaignServiceImpl implements CampaignService {

	@Autowired private CampaignMapper campaignMapper;
	
	@Override
	public Campaign getCurrentDBDatetime() {
		return campaignMapper.getCurrentDBDatetime();
	}
	
	@Override
	public List<Campaign> selectCampaignList() {
	    return campaignMapper.selectCampaignList();
	}
	
	@Override
	public Campaign selectCampaignInfo(int schdlId) {
		return campaignMapper.selectCampaignInfo(schdlId);
	}
	
	@Override
	public List<AddressGrp> selectAddressGrpList(String loginId) {
		return campaignMapper.selectAddressGrpList(loginId);
	}
	
	@Override
	public int insertCampaignSchdl(Campaign campaign) {
	    return campaignMapper.insertCampaignSchdl(campaign);
	}
	
	@Override
	public int updateCampaignSchdl(Campaign campaign) {
	    return campaignMapper.updateCampaignSchdl(campaign);
	}
	
	@Override
	public int updateSchdlFilePath(Campaign campaign) {
	    return campaignMapper.updateSchdlFilePath(campaign);
	}
	
	@Override
	public int updateTargetFilePath(Campaign campaign) {
		return campaignMapper.updateTargetFilePath(campaign);
	}
	
	@Override
	public int updateSchdlSendFlag(Campaign campaign) {
		return campaignMapper.updateSchdlSendFlag(campaign);
	}
    
}
