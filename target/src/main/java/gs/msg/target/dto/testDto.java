package gs.msg.target.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("testDto")
public class testDto {
    private String test;
    private String id;
}
