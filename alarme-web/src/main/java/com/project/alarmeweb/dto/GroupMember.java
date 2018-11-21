package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Date;

@Data
@Alias("GroupMember")
public class GroupMember {
    private String addrMbrId;
    private String addrGrpId;
    private String name;
    private String address;
    private String data1;
    private String data2;
    private String data3;
    private Date createdDt;
    private Date modifiedDt;
}
