package com.project.alarmeweb.controller.settings.code;


import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Code;
import com.project.alarmeweb.service.CodeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class CodeController extends BaseController {

    @Autowired private CodeService codeService;

	@RequestMapping(value={"/settings/code"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public ModelAndView code(){
        return new ModelAndView("settings/code/code");
    }

    @RequestMapping(value={"/settings/code/table"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String tableCode(@RequestParam Map<String, String> params){
        List<Code> codeList = codeService.getSystemCodeList("TYPE ASC", null);

        JSONObject result = new JSONObject();
        result.put("data", codeList);

        return result.toString();
    }

    @RequestMapping(value={"settings/code/condition"}, produces="application/json; charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String selectCode(@RequestParam Map<String, String> params){
        String type = params.get("type");
        String gubun = params.get("gubun");

        Code code = new Code();
        code.setType(type);
        code.setGubun(gubun);

        List<Code> codeList = codeService.getSystemCodeList("TYPE ASC", code);
        JSONObject result = new JSONObject();

        if(codeList.size() > 0){
            result.put("code","dup");
        }else{
            result.put("code", "noDup");
        }

        return result.toString();
    }

	@RequestMapping(value={"/settings/code"}, produces="text/html; charset=UTF-8", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteCode(@RequestBody Map<String, String> params){
        String type = params.get("type");
        String gubun = params.get("gubun");

	    Code code = new Code();
	    code.setType(type);
	    code.setGubun(gubun);

	    int deleteResult = codeService.deleteSystemCode(code);
        JSONObject result = new JSONObject();

        if(deleteResult > 0){
            result.put("code","success");
        }else{
            result.put("code", "fail");
        }

	    return result.toString();
    }

    @RequestMapping(value={"/settings/code"}, produces="text/html; charset=UTF-8", method = RequestMethod.POST)
    public ModelAndView insertCode(@RequestParam Map<String, String> params){
        String type = params.get("type");
        String gubun = params.get("gubun");
        String data1 = params.get("data1");
        String data2 = params.get("data2");
        String data3 = params.get("data3");

        Code code = new Code();
        code.setType(type);
        code.setGubun(gubun);
        code.setData1(data1);
        code.setData2(data2);
        code.setData3(data3);

        int insertResult = codeService.insertSystemCode(code);

        return new ModelAndView("/settings/code/code");
    }
}
