package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Receiver;
import com.project.alarmeweb.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
    public ResponseEntity getReceiverCheckGrpName(@RequestParam Map<String, Object> params){
        Map resultData = new HashMap();
        int cnt = receiverService.getReceivGrpNameCnt(params);
        resultData.put("data", (cnt > 0) ? "Y" : "N" );

        return new ResponseEntity(resultData, HttpStatus.OK);
    }

    @RequestMapping(value={"/mail/receiver/group"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public ResponseEntity getReceiver(){
        Map resultData = new HashMap();
        List<Receiver> receiverList = receiverService.getReceiverList();
        resultData.put("data", ( !CollectionUtils.isEmpty(receiverList) ) ? receiverList : new ArrayList<Receiver>()  );
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

}
