package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Realtime")
public class Realtime {
    private int schdlId;
    private int masterSchdlId;
    private String schdlName;
    private String sender;
    private String filePath;
    private String filePathHtml;
    private String sendGubun;
    private String sendType;
    private String sendFlag;
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
    private String regDate;
    private String uptDate;
}
