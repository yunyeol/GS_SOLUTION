package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class ReceiverController extends BaseController {

    @GetMapping(value={"/mail/receiver"}, produces="text/html; charset=UTF-8")
    public String campaign(Locale locale, Model model){

        return "mail/receiver/receiver";
    }
}
