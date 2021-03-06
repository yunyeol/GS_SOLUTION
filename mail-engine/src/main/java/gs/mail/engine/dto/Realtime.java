package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("Realtime")
public class Realtime extends Send implements Serializable {
    private long queId;
    private String schdlName;
    private String filePath;
    private String sendGubun;
    private String sendType;
    private String activeYn;

    private String sendFlag;
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
    private String targetYn;
    private String reserveDate;
    private String regDate;
}
