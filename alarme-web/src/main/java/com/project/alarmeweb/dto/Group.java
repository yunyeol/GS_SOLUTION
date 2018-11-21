package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@Alias("Group")
public class Group {
    private Long addrGrpId;
    private String addrGrpName;
    private String loginId;
    private Date createdDt;
    private Date modifiedDt;
    private Long addrGrpMbrCnt;
}
