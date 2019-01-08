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
    private String data1;
    private String data2;
    private String data3;
    private String regDate;

    private long rawId;

    private String fileUploadYn;
    private String targetFilePath;
}
