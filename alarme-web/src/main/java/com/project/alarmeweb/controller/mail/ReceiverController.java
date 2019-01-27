package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.PageMaker;
import com.project.alarmeweb.dto.Receiver;
import com.project.alarmeweb.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class ReceiverController extends BaseController {

    @Autowired
    private ReceiverService receiverService;

    @RequestMapping(value={"/mail/receiver"}, produces="text/html; charset=UTF-8", method = {RequestMethod.GET})
    public ModelAndView receiver(){ return new ModelAndView("mail/receiver/receiver"); }

    @RequestMapping(value={"/mail/receiver/group/checkname"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public ResponseEntity getReceiverCheckGrpName(@RequestParam Map<String, String> params){
        Map resultData = new HashMap();
        int cnt = receiverService.getReceivGrpNameCnt(params);
        resultData.put("data", (cnt > 0) ? "Y" : "N" );

        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public ResponseEntity getReceiver(){
        Map resultData = new HashMap();
        resultData.put("data", Optional.ofNullable(receiverService.getReceiverList()).orElse(new ArrayList<>()) );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
    public ResponseEntity addReceiver(@RequestBody Receiver receiver){
        Map resultData = new HashMap();
        int cnt = receiverService.addReceiver(receiver);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PUT})
    public ResponseEntity modifyReceiver(@RequestBody Receiver receiver){
        Map resultData = new HashMap();
        int cnt = receiverService.modifyReceiver(receiver);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group/{id}"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.DELETE})
    public ResponseEntity removeReceiver(@PathVariable Long id){
        Map resultData = new HashMap();
        int cnt = receiverService.removeReceiver(id);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    /** 그룹대상관리 */

    @RequestMapping(value={"/mail/receiver/group/detail"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public ResponseEntity getReceiverDetail(@RequestParam(value = "addrGrpId", required = false) Long addrGrpId, @RequestParam(value = "keyword", required = false) String keyword,
                                            @RequestParam(value = "currIdx", required = false) int currIdx, @RequestParam(value = "selected", required = false, defaultValue = "10") String selected){

        Map resultData = new HashMap();
        PageMaker receiverList = receiverService.getReceiverDeatil(addrGrpId, currIdx, selected, keyword);

        resultData.put("data", ( receiverList != null ) ? receiverList : PageMaker.getInstance()  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group/detail/checkRow"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public ResponseEntity ReceiverDetailCheckRow(Receiver receiver){
        Map resultData = new HashMap();
        int cnt = receiverService.getReceivDetCheckRowCnt(receiver);
        resultData.put("data", (cnt > 0) ? "Y" : "N" );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group/detail"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
    public ResponseEntity addReceiverDetail(@RequestBody Receiver receiver){
        Map resultData = new HashMap();
        int cnt = receiverService.addReceiverDetail(receiver);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group/detail"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PUT})
    public ResponseEntity modifyReceiverDetail(@RequestBody Receiver receiver){
        Map resultData = new HashMap();
        int cnt = receiverService.modifyReceiverDetail(receiver);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group//detail/{id}"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.DELETE})
    public ResponseEntity removeReceiverDetail(@PathVariable Long id){
        Map resultData = new HashMap();
        int cnt = receiverService.removeReceiverDetail(id);
        resultData.put("data", (cnt > 0) ? "SUCCESS" : "FAIL"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
