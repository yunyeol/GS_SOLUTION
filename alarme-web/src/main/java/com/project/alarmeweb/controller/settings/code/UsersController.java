package com.project.alarmeweb.controller.settings.code;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.User;
import com.project.alarmeweb.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class UsersController extends BaseController {
    @Autowired private UserService userService;

    @RequestMapping(value={"/settings/users"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String users(Model model){
        List<User> authList = userService.getAuthList();
        List<User> grpList = userService.getGrpList();

        model.addAttribute("authList",authList);
        model.addAttribute("grpList",grpList);
        return "settings/users/users";
    }

    @RequestMapping(value={"/settings/users/table"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String tableUsers(){
        List<User> userList = userService.getMemberList(null,"GM.LOGIN_ID ASC");

        JSONObject result = new JSONObject();
        result.put("data", userList);

        return result.toString();
    }

    @RequestMapping(value={"settings/users/condition"}, produces="application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String selectUser(@RequestParam Map<String, String> params){
        String loginId = params.get("loginId");

        User user = new User();
        user.setLoginId(loginId);

        List<User> userList = userService.getMemberList(loginId, null);
        JSONObject result = new JSONObject();

        if(userList.size() > 0){
            result.put("code","dup");
        }else{
            result.put("code", "noDup");
        }

        return result.toString();
    }

    @RequestMapping(value={"/settings/users"}, produces="text/html; charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUsers(@RequestBody Map<String, String> params){
        String loginId = params.get("loginId");

        User user = new User();
        user.setLoginId(loginId);

        int deleteResult = userService.deleteUsers(user);
        JSONObject result = new JSONObject();

        if(deleteResult > 0){
            result.put("code","success");
        }else{
            result.put("code", "fail");
        }

        return result.toString();
    }

    @RequestMapping(value={"/settings/users"}, produces="text/html; charset=UTF-8", method = RequestMethod.PUT)
    @ResponseBody
    public String updateActiveUsers(@RequestBody Map<String, String> params){
        String loginId = params.get("loginId");
        String activeYn = params.get("activeYn");

        User user = new User();
        user.setLoginId(loginId);
        user.setActiveYn(activeYn);

        int updateResult = userService.updateActiveUsers(user);
        JSONObject result = new JSONObject();

        if(updateResult > 0){
            result.put("code","success");
        }else{
            result.put("code", "fail");
        }

        return result.toString();
    }

    @RequestMapping(value={"/settings/users/auth"}, produces="text/html; charset=UTF-8", method = RequestMethod.PUT)
    @ResponseBody
    public String updateAuthUsers(@RequestBody Map<String, String> params){
        String loginId = params.get("loginId");
        String mbrAuthId = params.get("mbrAuthId");

        User user = new User();
        user.setLoginId(loginId);
        user.setMbrAuthId(Integer.parseInt(mbrAuthId));

        int updateResult = userService.updateAuthUsers(user);
        JSONObject result = new JSONObject();

        if(updateResult > 0){
            result.put("code","success");
        }else{
            result.put("code", "fail");
        }

        return result.toString();
    }

    @RequestMapping(value={"/settings/users/grp"}, produces="text/html; charset=UTF-8", method = RequestMethod.PUT)
    @ResponseBody
    public String updateGrpUsers(@RequestBody Map<String, String> params){
        String loginId = params.get("loginId");
        String mbrGrpId = params.get("mbrGrpId");

        User user = new User();
        user.setLoginId(loginId);
        user.setMbrGrpId(Integer.parseInt(mbrGrpId));

        int updateResult = userService.updateGrpUsers(user);
        JSONObject result = new JSONObject();

        if(updateResult > 0){
            result.put("code","success");
        }else{
            result.put("code", "fail");
        }

        return result.toString();
    }

    @RequestMapping(value={"/settings/users/pwd"}, produces="text/html; charset=UTF-8", method = RequestMethod.POST)
    public ModelAndView updatePwdUsers(Model model, @RequestParam Map<String, String> params){
        String loginId = params.get("loginId");
        String passwd = params.get("passwd");

        User user = new User();
        user.setLoginId(loginId);
        user.setPasswd(pwdEncode(passwd));

        int updateResult = userService.updatePwdUsers(user);

        List<User> authList = userService.getAuthList();
        List<User> grpList = userService.getGrpList();

        model.addAttribute("authList",authList);
        model.addAttribute("grpList",grpList);

        return new ModelAndView("/settings/users/users");
    }

    @RequestMapping(value={"/settings/users"}, produces="text/html; charset=UTF-8", method = RequestMethod.POST)
    public ModelAndView insertUsers(Model model, @RequestParam Map<String, String> params){
        String loginId = params.get("loginId");
        String passwd = params.get("passwd");
        String name = params.get("name");
        String grpId = params.get("insertSelectGrp");
        String authId = params.get("insertSelectAuth");

        User user = new User();
        user.setLoginId(loginId);
        user.setPasswd(pwdEncode(passwd));
        user.setMbrName(name);
        user.setMbrGrpId(Integer.parseInt(grpId));
        user.setMbrAuthId(Integer.parseInt(authId));

        int insertResult = userService.insertUsers(user);

        List<User> authList = userService.getAuthList();
        List<User> grpList = userService.getGrpList();

        model.addAttribute("authList",authList);
        model.addAttribute("grpList",grpList);

        return new ModelAndView("/settings/users/users");
    }
}
