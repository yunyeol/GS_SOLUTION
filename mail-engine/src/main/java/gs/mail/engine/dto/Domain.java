package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Domain")
public class Domain {
    private long id;
    private String domain;
    private int score;
    private String regDate;
}
