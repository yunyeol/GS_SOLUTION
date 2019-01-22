package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Target")
public class Target {
    private long schdlId;
    private long addressGrpId;
    private String sendGubun;
    private String sendType;

    private String addressGrpName;
    private int addressMbrId;
    private String mbrName;
    private String mbrAddress;
    private String map1;
    private String map2;
    private String map3;
    private String map4;
    private String map5;
    private String map6;
    private String map7;
    private String map8;
    private String map9;
    private String map10;
    private String regDate;

    private long rawId;

    private String targetFile;
}
