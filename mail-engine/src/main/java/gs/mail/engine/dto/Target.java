package gs.mail.engine.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Target")
public class Target {
    private long schdlId;
}
