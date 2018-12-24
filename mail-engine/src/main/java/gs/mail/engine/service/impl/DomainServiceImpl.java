package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Domain;
import gs.mail.engine.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DomainServiceImpl implements DomainService {

    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<Domain> selectDomainList() {
        List<Domain> selectDomainList = sqlSessionTemplate.selectList("SQL.Domain.selectDomainList");
        return selectDomainList;
    }
}
