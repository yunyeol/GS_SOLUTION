package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;


@Data
@Alias("Receiver")
public class Receiver {
    private Long addrMbrId;
    private Long addrGrpId;
    private String addrGrpName;
    private String loginId;
    private String name;
    private String address;
    private String data1;
    private String data2;
    private String data3;
    private Date createdDt;
    private Date modifiedDt;
    private Long addrGrpMbrCnt;
}
