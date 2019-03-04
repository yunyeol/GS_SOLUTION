package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Dashboard;
import com.project.alarmeweb.mapper.DashboardMapper;
import com.project.alarmeweb.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired private DashboardMapper dashboardMapper;

	@Override
	public List<Dashboard> getChart2w(Dashboard dashboard) {
	    return dashboardMapper.getChart2w(dashboard);
	}

	@Override
	public List<Dashboard> getTodaySendList() {
		return dashboardMapper.getTodaySendList();
	}

	@Override
	public List<Dashboard> getTodayReqList() {
		return dashboardMapper.getTodayReqList();
	}
}
