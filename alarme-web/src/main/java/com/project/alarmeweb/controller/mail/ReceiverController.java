package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Group;
import com.project.alarmeweb.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReceiverController extends BaseController {

    @Autowired
    private GroupService groupService;

    @GetMapping(value={"/mail/receiver"}, produces="text/html; charset=UTF-8")
    public ModelAndView receiver(){
        return new ModelAndView("mail/receiver/receiver");
    }

    @GetMapping(value={"/mail/receiver/group"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity group(){
        Map resultData = new HashMap();
        List<Group> groupList = groupService.getGroupList();
        resultData.put("data", (groupList != null) ? groupList : "[]"  );
        return new ResponseEntity(resultData, HttpStatus.OK);
    }
}
