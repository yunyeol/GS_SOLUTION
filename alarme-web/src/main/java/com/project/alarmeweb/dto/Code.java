package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Code")
public class Code {
    private String type;
    private String gubun;
    private String data1;
    private String data2;
    private String data3;
    private String regDate;
    private String uptDate;
}
