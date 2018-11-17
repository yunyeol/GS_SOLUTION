package com.project.alarmeweb.mapper;


import com.project.alarmeweb.dto.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeMapper {
    List<Code> getSystemCodeList(@Param("orderParam") String orderParam,
                                 @Param("code") Code code);
    int deleteSystemCode(@Param("code") Code code);
    int insertSystemCode(@Param("code") Code code);
}
