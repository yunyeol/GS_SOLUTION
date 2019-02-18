package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Campaign")
public class Campaign extends Send  {
    private long schdlId;
    private String schdlName;
    private long addressGrpId;
    private String sender;
    private String filePath;
    private String sendGubun;
    private String sendType;
    private String sendFlag;
    private String regDate;
    private String subject;
    private String senderName;
}
