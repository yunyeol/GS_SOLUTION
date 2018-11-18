package com.project.alarmeweb.controller;

import com.project.alarmeweb.controller.settings.code.CodeController;
import com.project.alarmeweb.dto.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    protected String pwdEncode(String pwd){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pwd);
    }

//    @ModelAttribute("authId")
//    protected String authId() {
//        UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return String.valueOf(userSession.getAuthId());
//    }
}
