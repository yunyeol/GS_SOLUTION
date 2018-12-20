package gs.mail.engine.service;

import gs.mail.engine.dto.Domain;

import java.util.List;

public interface DomainConnectService {
    List<Domain> selectDomainList();
}
