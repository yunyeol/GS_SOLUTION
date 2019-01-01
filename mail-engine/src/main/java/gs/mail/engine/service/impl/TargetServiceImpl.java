package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Target;
import gs.mail.engine.service.TargetService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TargetServiceImpl implements TargetService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;


    @Override
    public List<Target> selectTargetList() {
        List<Target> selectTargetList = sqlSessionTemplate.selectList("SQL.Target.selectTargetList");
        return selectTargetList;
    }
}
