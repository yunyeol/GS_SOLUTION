package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.Code;
import com.project.alarmeweb.mapper.CodeMapper;
import com.project.alarmeweb.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeServiceImpl implements CodeService {
    @Autowired private CodeMapper codeMapper;

    @Override
    public List<Code> getSystemCodeList(String orderParam, Code code) {
        return codeMapper.getSystemCodeList(orderParam, code);
    }

    @Override
    public int deleteSystemCode(Code code) {
        return codeMapper.deleteSystemCode(code);
    }

    @Override
    public int insertSystemCode(Code code) {
        return codeMapper.insertSystemCode(code);
    }
}
