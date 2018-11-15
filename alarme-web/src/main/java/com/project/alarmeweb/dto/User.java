package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("User")
public class User {
    private String loginId;
    private String passwd;
    private int mbrGrpId;
    private int mbrAuthId;
    private String mbrName;
    private String activeYn;
    private String regDate;
    private String uptDate;
	
}
