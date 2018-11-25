package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Realtime;
import gs.mail.engine.service.RealtimeSendService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RealtimeSendServiceImpl implements RealtimeSendService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<Realtime> selectRealtimeSchdlList() {
        List<Realtime> selectRealtimeSendList = sqlSessionTemplate.selectList("SQL.RealitmeSend.selectRealtimeSchdl");
        return selectRealtimeSendList;
    }
}
