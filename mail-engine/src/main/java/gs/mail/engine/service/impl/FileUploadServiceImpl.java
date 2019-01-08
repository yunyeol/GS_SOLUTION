package gs.mail.engine.service.impl;

import gs.mail.engine.dto.Target;
import gs.mail.engine.service.FileUploadService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<Target> selectFileUploadSchdlList() {
        List<Target> selectFileUploadSchdlList = sqlSessionTemplate.selectList("SQL.FILE.UPLOAD.selectFileUploadSchdlList");
        return selectFileUploadSchdlList;
    }
}
