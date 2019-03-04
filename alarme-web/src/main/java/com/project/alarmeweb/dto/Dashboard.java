package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Dashboard")
public class Dashboard {
    private String chartType;
    private String startDate;
    private String endDate;
    private String workday;
    private long sendCnt = 0;
    private long successCnt = 0;
    private long failCnt = 0;
    private long clickCnt = 0;
    private long openCnt = 0;
    private String sendType;
    private String schdlName;
    private String reserveDate;
    private String status;
}
