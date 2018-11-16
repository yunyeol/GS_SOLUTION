package com.project.alarmeweb.service;

import com.project.alarmeweb.dto.Code;

import java.util.List;

public interface CodeService {
    List<Code> getSystemCodeList(String orderParam);
    int deleteSystemCode(Code code);
}
