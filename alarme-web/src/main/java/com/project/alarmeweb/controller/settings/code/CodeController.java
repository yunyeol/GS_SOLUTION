package com.project.alarmeweb.controller.settings.code;


import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Code;
import com.project.alarmeweb.service.CodeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class CodeController extends BaseController {

    @Autowired private CodeService codeService;

	@GetMapping(value={"/settings/code"}, produces="text/html; charset=UTF-8")
    public String code(Locale locale, Model model){
//        List<Code> codeList = codeService.getSystemCodeList("TYPE ASC");
//
//        model.addAttribute("codeList", codeList);
        return "settings/code/code";
    }

    @GetMapping(value={"/settings/code/ajax"}, produces="text/html; charset=UTF-8")
    @ResponseBody
    public String codeAjax(Locale locale, Model model){
        List<Code> codeList = codeService.getSystemCodeList("TYPE ASC");

        JSONObject result = new JSONObject();
        result.put("data", codeList);

        logger.info(result.toString());
        return result.toString();
    }

	@DeleteMapping(value={"/settings/code"})
    @ResponseBody
    public String deleteCode(Locale locale, Model model, @RequestBody Map<String, String> params){
        String type = params.get("type");
        String gubun = params.get("gubun");

        logger.info("### : {}", type);
        logger.info("### : {}", gubun);

	    Code code = new Code();
	    code.setType(type);
	    code.setGubun(gubun);

	    int deleteResult = codeService.deleteSystemCode(code);
//        JSONObject result = new JSONObject();
//
//        if(deleteResult > 0){
//            result.put("code","success");
//        }else{
//            result.put("code", "fail");
//        }
	    return "";
    }
}
