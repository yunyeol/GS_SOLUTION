package gs.mail.engine.service;

import gs.mail.engine.dto.Realtime;

import java.util.List;

public interface RealtimeSendService {
    List<Realtime> selectRealtimeSchdlList();
}
