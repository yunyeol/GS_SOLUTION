package com.project.alarmeweb.controller.mail;

import com.project.alarmeweb.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class CampaignController extends BaseController {

    @GetMapping(value={"/mail/send/campaign"}, produces="text/html; charset=UTF-8")
    public String campaign(Locale locale, Model model){

        return "mail/send/campaign/campaign";
    }
}
