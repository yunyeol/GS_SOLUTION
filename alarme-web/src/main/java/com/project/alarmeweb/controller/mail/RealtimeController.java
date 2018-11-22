package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Realtime;
import com.project.alarmeweb.service.RealtimeService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Controller
public class RealtimeController extends BaseController {

    @Value("${html.save.path}") private String htmlPath;
    @Value("${html.header}") private String htmlHeader;
    @Value("${html.footer}") private String htmlFooter;

    @Autowired private RealtimeService realtimeService;

    @RequestMapping(value={"/mail/send/realtime"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String realtime(){
        return "mail/send/realtime/realtime";
    }

    @RequestMapping(value={"/mail/send/realtime/setting"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String realtimeSetting(){
        return "mail/send/realtime/realtimeSetting";
    }

    @RequestMapping(value={"/mail/send/realtime/setting/save"}, produces="text/html; charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public String realtimeSettingSave(@RequestBody Map<String, String> params){
        logger.info("param : {}", params.get("title"));
        logger.info("param : {}", params.get("contents"));
        logger.info("param : {}", params.get("sender"));
        logger.info("param : {}", params.get("send_gubun"));
        logger.info("param : {}", params.get("send_type"));

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
        realtime.setMasterSchdlId("M");
        realtime.setSendFlag(00);

        String htmlContents = htmlHeader + contents + htmlFooter;
        String fileName = "html_"+loginId()+"_"+System.currentTimeMillis()+".html";

        realtime.setFilePath(htmlPath+"/"+fileName);

        try {
            File file = new File(htmlPath+"/"+fileName);
            FileWriter fw = new FileWriter(file, true);

            fw.write(htmlContents);
            fw.flush();

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int insertResult = realtimeService.insertRealtimeSchdl(realtime);
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
}
