package com.project.alarmeweb.controller.settings.code;


import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.Code;
import com.project.alarmeweb.service.CodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;

@Controller
public class CodeController extends BaseController {

    @Autowired private CodeService codeService;

	@GetMapping(value={"/settings/code"}, produces="text/html; charset=UTF-8")
	public String code(Locale locale, Model model){
        List<Code> codeList = codeService.getSystemCodeList();

	    model.addAttribute("codeList", codeList);
		return "settings/code/code";
	}
}
