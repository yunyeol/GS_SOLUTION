package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.PageMaker;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RealtimeController extends BaseController {

    @Value("${html.save.path}") private String htmlPath;
    @Value("${html.header}") private String htmlHeader;
    @Value("${html.footer}") private String htmlFooter;

    @Autowired private RealtimeService realtimeService;

    @RequestMapping(value={"/mail/send/realtime"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String realtime(Model model, @RequestParam(value = "currIdx", required = false, defaultValue = "1") int currIdx
                          ,@RequestParam(value = "isAjax", required = false, defaultValue = "false") boolean isAjax){
        PageMaker pageMaker = realtimeService.selectRealtimeMasterPagingList(new HashMap<>(), currIdx);

        if( !CollectionUtils.isEmpty( pageMaker.getContentList() ) ){
            pageMaker.getContentList().stream().filter(r->{
                Realtime realtime = (Realtime)r;
                return StringUtils.isNotEmpty(realtime.getSendFlag());
            }).map(r->{
                Realtime realtime = (Realtime)r;
                String sendFlagStr = "";
                switch(realtime.getSendFlag()){
                    case "00": sendFlagStr = "준비"; break;
                    case "10": sendFlagStr = "타게팅 - 진행"; break;
                    case "11": sendFlagStr = "타게팅 - 완료"; break;
                    case "12": sendFlagStr = "타게팅 - 실패"; break;
                    case "20": sendFlagStr = "발송예약"; break;
                    case "30": sendFlagStr = "발송중"; break;
                    case "40": sendFlagStr = "발송완료"; break;
                    case "50": sendFlagStr = "발송실패"; break;
                    default:   sendFlagStr = ""; break;
                }
                realtime.setSendFlagStr(sendFlagStr);
                return realtime;
            }).collect(Collectors.toList());
        }

        model.addAttribute("realtimeMasterList", pageMaker.getContentList() );
        model.addAttribute("realtimeMasterTotCnt", pageMaker.getTotCnt() );

//        return "mail/send/realtime/realtime";
        return ( !isAjax ) ? "mail/send/realtime/realtime" : "mail/send/realtime/realtimeDashBoard";
    }

    @RequestMapping(value={"/mail/send/realtime/setting"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String realtimeSetting(@RequestParam(value = "viewType", defaultValue = "",required = false) String viewType,
                                  @RequestParam(value = "schdlId", defaultValue = "-1",required = false) long schdlId, Model model) throws Exception {
        if( StringUtils.isNotEmpty(viewType) && "DETAIL".equals(viewType) ){
            if( schdlId < 0 ){
                throw new Exception("schdlId is null");
            }
            Map<String, Object> params = new HashMap<>();
            params.put("schdlId",schdlId);
            List<Realtime> realtimeList = realtimeService.selectRealtimeMasterList(params);
            if( !CollectionUtils.isEmpty(realtimeList) ){
                realtimeList = realtimeList.stream().filter(r->StringUtils.isNotEmpty(r.getFilePath())).map(r->{
                  File file = new File(r.getFilePath());
                  if( file.isFile() ){
                      try{
                          BufferedReader bufferedReader =  new BufferedReader(new FileReader(r.getFilePath()));
                          StringBuilder stringBuilder = new StringBuilder();
                          String line = "";
                          while((line = bufferedReader.readLine()) != null){
                              stringBuilder.append(line);
                          }
                          r.setFilePathHtml(stringBuilder.toString());
                          bufferedReader.close();
                      }catch (FileNotFoundException e) {
                          logger.info("e :",e);
                      }catch(IOException e){
                          logger.info("e :",e);
                      }
                  }
                  return  r;
                }).limit(1).collect(Collectors.toList());
            }

            model.addAttribute("entryData",Optional.ofNullable(realtimeList).orElse(null));
        }

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
            BufferedWriter fw = new BufferedWriter(new FileWriter(file, true));
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

    @RequestMapping(value={"/mail/send/realtime/setting/save"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PUT})
    @ResponseBody
    public Map<String,String> realtimeSettingModify(@RequestBody Realtime realtime){
        Map<String,String> resultData = new HashMap<>();
        realtime = Optional.ofNullable(realtime).filter(r->r.getSchdlId() > 0).orElse(null);
        if( Objects.isNull(realtime) ){
            resultData.put("data","FAIL");
            return resultData;
        }
        int cnt = realtimeService.updateRealtimeSetting(realtime);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );

        return resultData;
    }

    @RequestMapping(value={"/mail/send/realtime/setting/activeYn"}, produces= MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PUT})
    @ResponseBody
    public Map<String,String> realtimeActiveYn(@RequestBody Realtime realtime){
        Map<String,String> resultData = new HashMap<>();
        Realtime reaTime = Optional.ofNullable(realtime).filter(r->StringUtils.isNotEmpty(r.getActiveYn())).filter(r->r.getSchdlId() > 0).orElse(null);
        if( Objects.isNull(reaTime) ){
            resultData.put("data","FAIL");
            return resultData;
        }
        int cnt = realtimeService.updateActiveYn(realtime);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );

        return resultData;
    }

    @RequestMapping(value={"/mail/send/realtime/{schdlId}"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.DELETE})
    @ResponseBody
    public Map<String,String> removeRealtime(@PathVariable Long schdlId){
        Map<String,String> resultData = new HashMap<>();
        int cnt = realtimeService.removeRealtime(schdlId);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return resultData;
    }

}
