package com.project.alarmeweb.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class UserController extends  BaseController{

    @GetMapping(value={"/user/profile"}, produces="text/html; charset=UTF-8")
    public String login(Locale locale, Model model){
        return "user/profile";
    }

}