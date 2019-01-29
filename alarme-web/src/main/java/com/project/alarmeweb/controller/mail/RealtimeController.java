package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Realtime;
import com.project.alarmeweb.service.RealtimeService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Controller
public class RealtimeController extends BaseController {

    @Value("${html.save.path}") private String htmlPath;
    @Value("${html.header}") private String htmlHeader;
    @Value("${html.footer}") private String htmlFooter;

    @Autowired private RealtimeService realtimeService;

    @RequestMapping(value={"/mail/send/realtime"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String realtime(Model model){
        List<Realtime> realtimeMasterList = realtimeService.selectRealtimeMasterList();

        String sendFlagStr = "";
        for (Realtime realtime : realtimeMasterList){
            if(realtime.getSendFlag().equals("00")){
                sendFlagStr = "준비";
            }else if(realtime.getSendFlag().equals("10")){
                sendFlagStr = "타게팅 - 진행";
            }else if(realtime.getSendFlag().equals("11")){
                sendFlagStr = "타게팅 - 완료";
            }else if(realtime.getSendFlag().equals("12")){
                sendFlagStr = "타게팅 - 실패";
            }else if(realtime.getSendFlag().equals("20")){
                sendFlagStr = "발송예약";
            }else if(realtime.getSendFlag().equals("30")){
                sendFlagStr = "발송중";
            }else if(realtime.getSendFlag().equals("40")){
                sendFlagStr = "발송완료";
            }else if(realtime.getSendFlag().equals("50")){
                sendFlagStr = "발송실패";
            }
            realtime.setSendFlagStr(sendFlagStr);
        }

        model.addAttribute("realtimeMasterList", realtimeMasterList);
        return "mail/send/realtime/realtime";
    }

    @RequestMapping(value={"/mail/send/realtime/setting"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String realtimeSetting(){
        return "mail/send/realtime/realtimeSetting";
    }

    @RequestMapping(value={"/mail/send/realtime/setting/save"}, produces="text/html; charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public String realtimeSettingSave(@RequestBody Map<String, String> params){
        String schdlName = params.get("title");
        String sender = params.get("sender");
        String sendGubun = params.get("send_gubun");
        String sendType = params.get("send_type");
        String contents = params.get("contents");

        Realtime realtime = new Realtime();
        realtime.setSchdlName(schdlName);
        realtime.setSender(sender);
        realtime.setSendGubun(sendGubun);
        realtime.setSendType(sendType);
        realtime.setMasterSchdlId(0);
        realtime.setSendFlag("00");

        String htmlContents = htmlHeader + contents + htmlFooter;
        String fileName = loginId()+"_"+System.currentTimeMillis()+".html";

        realtime.setFilePath(htmlPath+"/"+fileName);
        File file = null;
        try {
            file = new File(htmlPath+"/"+fileName);
            FileWriter fw = new FileWriter(file, true);

            fw.write(htmlContents);
            fw.flush();

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int insertResult = 0;
        if(file.isFile()){
            insertResult  = realtimeService.insertRealtimeSchdl(realtime);
        }

        JSONObject result = new JSONObject();
        try{
            if(insertResult > 0){
                result.put("code","success");
            }else{
                result.put("code", "fail");
            }
        }catch (JSONException e){
            logger.error("{}",e);
        }

        return result.toString();
    }

    @RequestMapping(value={"/mail/send/realtime/setting/activeYn"}, produces= MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PUT})
    @ResponseBody
    public Map realtimeActiveYn(@RequestBody Realtime realtime){
        Map resultData = new HashMap<>();
        Realtime reaTime = Optional.ofNullable(realtime).filter(row->StringUtils.isNotEmpty(row.getActiveYn())).filter(row->row.getSchdlId() > 0).orElse(null);
        if( Objects.isNull(reaTime) ){
            resultData.put("data","FAIL");
            return resultData;
        }
        int cnt = realtimeService.updateActiveYn(realtime);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );

        return resultData;
    }

}
