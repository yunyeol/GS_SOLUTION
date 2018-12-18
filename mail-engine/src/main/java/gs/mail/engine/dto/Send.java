package gs.mail.engine.dto;

import lombok.Data;

@Data
public class Send {
    protected String receiver;
    protected String sender;
    protected String title;
    protected String contents;
    private String uuid;
}
