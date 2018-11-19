package com.project.alarmeweb.controller;

import com.project.alarmeweb.controller.settings.code.CodeController;
import com.project.alarmeweb.dto.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    protected String pwdEncode(String pwd){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pwd);
    }

    @ModelAttribute("role")
    protected boolean role() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> role = authentication.getAuthorities();

        boolean isRole = false;
        if(role.toString().equals("[ROLE_ADMIN]")){
            isRole = true;
        }
        return isRole;
    }

    @ModelAttribute("loginId")
    private String  loginId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }
}
