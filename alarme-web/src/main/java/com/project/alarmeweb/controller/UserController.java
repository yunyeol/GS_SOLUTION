package com.project.alarmeweb.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.project.alarmeweb.dto.User;
import com.project.alarmeweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Locale;

@Controller
public class UserController extends  BaseController{

    @Autowired
    UserService userService;

    @GetMapping(value={"/user/profile"}, produces="text/html; charset=UTF-8")
    public String login(Locale locale, Model model){
        List<User> userList = userService.getMemberList(loginId(), null);
        model.addAttribute("memberList",userList);
        return "user/profile";
    }

}