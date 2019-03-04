package com.project.alarmeweb.mapper;

import com.project.alarmeweb.dto.Dashboard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardMapper {

    List<Dashboard> getChart2w(@Param("dashboard") Dashboard dashboard);

    List<Dashboard> getTodaySendList();

    List<Dashboard> getTodayReqList();

}
