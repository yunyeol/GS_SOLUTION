package gs.mail.engine.service;


import gs.mail.engine.dto.Target;

import java.util.List;

public interface TargetService {
    List<Target> selectTargetList();

    boolean isRunningChk(long schdlId);
    void setRunnging(long schdlId, boolean isRun);
}
