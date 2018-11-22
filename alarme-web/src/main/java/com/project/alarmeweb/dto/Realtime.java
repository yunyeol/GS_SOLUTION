package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Realtime")
public class Realtime {
    private int schdlId;
    private String masterSchdlId;
    private String schdlName;
    private String sender;
    private String filePath;
    private String sendGubun;
    private String sendType;
    private int sendFlag;
    private int targetCnt;
    private int sendCnt;
    private int successCnt;
    private int failCnt;
    private int openCnt;
    private int clickCnt;
    private String reserveDate;
    private int divideCnt;
    private String regDate;
    private String uptDate;
}
