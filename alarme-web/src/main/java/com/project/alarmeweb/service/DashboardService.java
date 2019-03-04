package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Dashboard;

import java.util.List;

public interface DashboardService {

	List<Dashboard> getChart2w(Dashboard dashboard);

	List<Dashboard> getTodaySendList();

	List<Dashboard> getTodayReqList();
	
}
