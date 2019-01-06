package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Send")
public class Send {
    private long schdlId;
    private long masterSchdlId;
    private String receiver;
    private String sender;
    private String title;
    private String contents;
    private String uuid;

    private String logFileName;
    private String lineNumber;
}
