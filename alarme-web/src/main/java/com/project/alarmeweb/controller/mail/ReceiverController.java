package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Receiver;
import com.project.alarmeweb.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReceiverController extends BaseController {

    @Autowired
    private ReceiverService receiverService;

    @RequestMapping(value={"/mail/receiver"}, produces="text/html; charset=UTF-8", method = {RequestMethod.GET})
    public ModelAndView receiver(Model model){ return new ModelAndView("mail/receiver/receiver"); }

    @RequestMapping(value={"/mail/receiver/group"}, produces=MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET})
    public ResponseEntity group(){
        Map resultData = new HashMap();
        List<Receiver> receiverList = receiverService.getReceiverList();
        resultData.put("data", (receiverList != null) ? receiverList : new ArrayList<Receiver>()  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
