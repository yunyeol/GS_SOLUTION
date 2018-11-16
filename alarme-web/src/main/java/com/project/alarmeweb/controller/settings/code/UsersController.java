package com.project.alarmeweb.controller.settings.code;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.User;
import com.project.alarmeweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Locale;

@Controller
public class UsersController extends BaseController {
    @Autowired private UserService userService;

    @GetMapping(value={"/settings/users"}, produces="text/html; charset=UTF-8")
    public String users(Locale locale, Model model){
        List<User> userList = userService.getMemberList(null,"GM.LOGIN_ID ASC");

        model.addAttribute("userList", userList);
        return "settings/code/users";
    }
}
