package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Domain")
public class Domain {
    private String domain;
    private String score;
    private String regDate;
}
