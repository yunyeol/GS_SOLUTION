package com.project.alarmeweb.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("AddressGrp")
public class AddressGrp {
    private int addressGrpId;
    private String addressGrpName;
}
