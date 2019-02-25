package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Campaign;
import com.project.alarmeweb.dto.Dashboard;
import com.project.alarmeweb.service.DashboardService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class DashBoardController extends BaseController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping(value={"/mail/dashboard"}, produces="text/html; charset=UTF-8")
    public String dashboard(Locale locale, Model model){

        return "mail/dashboard/dashboard";
    }

    @ResponseBody
    @RequestMapping(value= {"/mail/dashboard/get2wChartData"}, produces="text/html; charset=UTF-8", method = {RequestMethod.POST})
    public String get2wChartData(@RequestBody Map<String, String> params) {

        JSONObject result = new JSONObject();

        // TO-DO : 로그인 기능 구현 후 로그인 사용자 ID 조건 추가 필요
        int intv = 90;  // 기획상 2주간 데이터를 조회해야 하나 데이터가 많지 않아 임시로 3개월 분 데이터 조회
        Dashboard dashboard = new Dashboard();

        dashboard.setChartType(params.get("chartType"));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        dashboard.setEndDate(sdf.format(cal.getTime()));
        cal.add(Calendar.DATE, intv*(-1));
        dashboard.setStartDate(sdf.format(cal.getTime()));

        List<String> dateArray = new ArrayList<>();
        dateArray.add(sdf.format(cal.getTime()));

        for (int i = 0; i < intv; i++) {
            cal.add(Calendar.DATE, 1);
            dateArray.add(sdf.format(cal.getTime()));
        }

        List<Dashboard> dataList = dashboardService.getChart2w(dashboard);
        List<Dashboard> workdayDataList = new ArrayList<>();
        int dateArraySize = dateArray.size();
        int dataListSize = dataList.size();
        boolean isExistData = false;
        for (int j = 0; j < dateArraySize; j++) {
            isExistData = false;
            for (int k = 0; k < dataListSize; k++) {
                if (dateArray.get(j).equals(dataList.get(k).getWorkday())) {
                    workdayDataList.add(dataList.get(k));
                    isExistData = true;
                    break;
                }
            }
            if (!isExistData) {
                Dashboard item = new Dashboard();
                item.setWorkday(dateArray.get(j));
                workdayDataList.add(item);
            }
        }

        // 데이터 재가공 : 일자 기준 데이터 -> 분류(전체/성공/실패/오픈/클릭) 기준 데이터
        List<Long> sendCntList = new ArrayList<>();
        List<Long> successCntList = new ArrayList<>();
        List<Long> failCntList = new ArrayList<>();
        List<Long> openCntList = new ArrayList<>();
        List<Long> clickCntList = new ArrayList<>();

        for (Dashboard item : workdayDataList) {
            sendCntList.add(item.getSendCnt());
            successCntList.add(item.getSuccessCnt());
            failCntList.add(item.getFailCnt());
            openCntList.add(item.getOpenCnt());
            clickCntList.add(item.getClickCnt());
        }

        result.put("dateArray", dateArray);
        result.put("sendCntList", sendCntList);
        result.put("successCntList", successCntList);
        result.put("failCntList", failCntList);
        result.put("openCntList", openCntList);
        result.put("clickCntList", clickCntList);

        return result.toString();
    }

}
