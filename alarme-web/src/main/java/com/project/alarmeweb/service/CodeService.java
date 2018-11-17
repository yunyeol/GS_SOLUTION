package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Code;

import java.util.List;

public interface CodeService {
    List<Code> getSystemCodeList(String orderParam, Code code);
    int deleteSystemCode(Code code);
    int insertSystemCode(Code code);
}
