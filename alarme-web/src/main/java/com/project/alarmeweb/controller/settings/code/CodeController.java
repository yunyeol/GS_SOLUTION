package com.project.alarmeweb.controller.settings.code;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class CodeController {

	private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

	@GetMapping(value={"/settings/code"}, produces="text/html; charset=UTF-8")
	public String code(Locale locale, Model model){
		logger.debug("{}", "code page");

		return "settings/code/code";
	}

}
