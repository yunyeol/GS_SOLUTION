package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Campaign")
public class Campaign {
    private int schdlId;
    private int masterSchdlId;
    private String schdlName;			// 캠페인 스케줄명
    private String subject;				// 메일 제목
    private int addressGrpId;
    private String sender;
    private String senderName;
    private String filePath;
    private String targetFilePath;
    private String sendGubun;
    private String sendType = "C_F";
    private String sendFlag = "00";
    private String sendFlagStr;
    private int targetCnt;
    private int sendCnt;
    private int successCnt;
    private int failCnt;
    private int openCnt;
    private int clickCnt;
    private int divideCnt;
    private String activeYn;
    private String reserveDate;
    private String reserveTime;
    private String regDate;
    private String uptDate;
}
