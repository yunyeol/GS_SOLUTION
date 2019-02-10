package com.project.alarmeweb.controller.mail;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.alarmeweb.controller.BaseController;
import com.project.alarmeweb.dto.AddressGrp;
import com.project.alarmeweb.dto.Campaign;
import com.project.alarmeweb.service.CampaignService;



@Controller
public class CampaignController extends BaseController {
	
	@Value("${html.save.path}") private String htmlPath;
    @Value("${html.header}") private String htmlHeader;
    @Value("${html.footer}") private String htmlFooter;
    
    @Value("${target.file.path}") private String targetFilePath;
	
	@Autowired CampaignService campaignService;

    @RequestMapping(value={"/mail/send/campaign"}, produces="text/html; charset=UTF-8", method=RequestMethod.GET)
    public String campaign(Locale locale, Model model){
    	List<Campaign> campaignList = campaignService.selectCampaignList();

        String sendFlagStr = "";
        for (Campaign campaign : campaignList){
            if(campaign.getSendFlag().equals("00")){
                sendFlagStr = "준비";
            }else if(campaign.getSendFlag().equals("10")){
                sendFlagStr = "타게팅 - 진행";
            }else if(campaign.getSendFlag().equals("11")){
                sendFlagStr = "타게팅 - 완료";
            }else if(campaign.getSendFlag().equals("12")){
                sendFlagStr = "타게팅 - 실패";
            }else if(campaign.getSendFlag().equals("20")){
                sendFlagStr = "발송예약";
            }else if(campaign.getSendFlag().equals("30")){
                sendFlagStr = "발송중";
            }else if(campaign.getSendFlag().equals("40")){
                sendFlagStr = "발송완료";
            }else if(campaign.getSendFlag().equals("50")){
                sendFlagStr = "발송실패";
            }
            campaign.setSendFlagStr(sendFlagStr);
        }

        model.addAttribute("campaignList", campaignList);
    	
        return "mail/send/campaign/campaign";
    }
    

    @RequestMapping(value={"/mail/send/campaign/setting"}, produces="text/html; charset=UTF-8", method = RequestMethod.GET)
    public String campaignSetting(Model model, @RequestParam Map<String, String> params){
		int schdlId = "undefined".equals(params.get("schdlId")) ? 0 : Integer.parseInt(StringUtils.defaultString(params.get("schdlId"), "0"));
		
		List<AddressGrp> addressGrpList = campaignService.selectAddressGrpList(loginId());
		
		Campaign campaignInfo;
		
		if (schdlId > 0) {
			// 기존 캠페인 수정
			campaignInfo = campaignService.selectCampaignInfo(schdlId);

			// 메일 본문 내용 가져옴
            String filePath = campaignInfo.getFilePath();

            Path contentPath = Paths.get(filePath);
            Charset charset = StandardCharsets.UTF_8;

            List<String> contentLines = new ArrayList<>();

            try {
                contentLines = Files.readAllLines(contentPath, charset);
            } catch (IOException ioe) {
                ioe.getStackTrace();
            }

            String mailContent = "";
            for (String singleLine : contentLines) {
                mailContent += singleLine;
            }

            campaignInfo.setMailContent(mailContent);

		} else {
			// 신규 캠페인 등록 : 현재 DB 시간 기본 세팅
			//campaignInfo = campaignService.getCurrentDBDatetime();    // 스크립트에서 세팅하도록 수정
			campaignInfo = new Campaign();
			campaignInfo.setSchdlId(0);
			campaignInfo.setSendType("C_F");
			campaignInfo.setSendFlag("00");
		}
		
		model.addAttribute("addressGrpList", addressGrpList);
		model.addAttribute("campaignInfo", campaignInfo);
		
		return "mail/send/campaign/campaignSetting";
    }

	@ResponseBody
	@RequestMapping(value = {"/mail/send/campaign/setting"}, method = {RequestMethod.DELETE})
	public String campaignDelete(@RequestParam Map<String, String> params) {
		// 캠페인 삭제 - UPDATE DEL_YN = Y

		int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(params.get("schdlId"), "0"));

		if (campaignService.deleteCampaign(schdlId) > 0) {
            return makeResultStr("success", "");
		} else {
            return makeResultStr("fail", "");
		}
	}
    
    @ResponseBody
    @RequestMapping(value= {"/mail/send/campaign/setting/save"}, produces="text/html; charset=UTF-8", method = {RequestMethod.POST})
    public String campaignSettingSave(@RequestBody Map<String, String> params) {
    	JSONObject result = new JSONObject();
    	
    	Campaign campaign = new Campaign();
    	campaign.setSchdlName(params.get("schdlName"));
    	campaign.setSubject(params.get("subject"));
    	campaign.setSender(StringUtils.defaultString(params.get("sender"), ""));
    	campaign.setSenderName(params.get("senderName"));
    	campaign.setReserveDate(params.get("reserveDate"));
    	campaign.setReserveTime(params.get("reserveTime"));
    	campaign.setSendType(StringUtils.defaultIfEmpty(params.get("sendType"), "C_N"));
    	campaign.setFilePath("PROCESSING");

    	int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(params.get("schdlId"), "0"));
    	
    	// Exist schedule?
    	if (schdlId > 0) {
    		// Exist schedule id
    		campaign.setSchdlId(schdlId);
    		// TO-DO : 기존 스케줄 업데이트
    		campaignService.updateCampaignSchdl(campaign);
    	} else {
    		// Generate schedule id
    		if (campaignService.insertCampaignSchdl(campaign) < 1) {
				result.put("result", "fail");
				result.put("desc", "create schedule fail");
				return result.toString();
			}
    	}
    	
    	String contents = params.get("contents");
    	String filePath = "";
    	try {
            filePath = saveMailContent(contents, campaign.getSchdlId());
        } catch(Exception e) {
    	    // 파일 생성 실패(파일 생성 중 에러 발생)
            result.put("result", "fail");
            result.put("desc", "create mail content file fail");
            return result.toString();
        }

        // 파일 생성 후 파일명만 업데이트
        campaign.setFilePath(filePath);
        campaignService.updateSchdlFilePath(campaign);
    	
    	result.put("result", "success");
    	result.put("desc", campaign.getSchdlId());
    	
    	return result.toString();
    }
    
    @ResponseBody
    @RequestMapping(value = {"/mail/send/campaign/setting/tgt/fileUpload"}, method = {RequestMethod.POST})
    public String uploadTargetFile(@RequestParam("schdlId") String schdlIdStr, @RequestParam("targetFile") MultipartFile targetFile) {
    	int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(schdlIdStr, "0"));
    	if (schdlId < 1) {
            return makeResultStr("fail", "Not exist schedule id");
    	}

    	String originalFilenameExtension = FilenameUtils.getExtension(targetFile.getOriginalFilename());
    	String generateFileName = schdlId + "_" + loginId() + "_" + System.currentTimeMillis() + "." + originalFilenameExtension;

    	try {
    		targetFile.transferTo(new File(targetFilePath + "/"+ generateFileName));

    		// 파일 업로드 후 업로드 파일명 업데이트
    		Campaign campaign = new Campaign();
    		campaign.setSchdlId(schdlId);
    		campaign.setTargetFilePath(generateFileName);

    		campaignService.updateTargetFilePath(campaign);
    	} catch (Exception e) {
    		e.printStackTrace();
            return makeResultStr("fail", "Create file error");
    	}

        return makeResultStr("success", "");
    }

    @ResponseBody
    @RequestMapping(value = {"/mail/send/campaign/setting/tgt/start"}, method = {RequestMethod.POST})
    public String startTargeting(@RequestBody Map<String, String> params) {
    	// 타겟팅 예약 - SEND_FLAG : 10
    	int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(params.get("schdlId"), "0"));
    	if (schdlId < 1) {
            return makeResultStr("fail", "Not exist schedule id");
    	}

    	if (StringUtils.isEmpty(params.get("sendType"))
                || ("C_D".equals(params.get("sendType")) && StringUtils.isEmpty(params.get("addressGrpId")) )) {
            return makeResultStr("fail", "Parameter not exist");
        }
    	
    	Campaign campaign = new Campaign();
    	campaign.setSchdlId(schdlId);
    	campaign.setSendType(params.get("sendType"));
    	if ("C_D".equals(campaign.getSendType())) {
    	    campaign.setAddressGrpId(Integer.parseInt(params.get("addressGrpId")));
        }
    	campaign.setSendFlag("10");
    	
    	if (campaignService.updateTargetStatus(campaign) > 0)
            return makeResultStr("success", "");
    	else
            return makeResultStr("fail", "DB update error");
	}

	// 발송 예약
	@ResponseBody
	@RequestMapping(value = {"/mail/send/campaign/setting/send"}, method = {RequestMethod.POST})
	public String campaignSend(@RequestBody Map<String, String> params) {
		int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(params.get("schdlId"), "0"));
		if (schdlId < 1) {
			return makeResultStr("fail", "Not exist schedule id");
		}
		
		// 발송 전 다시 캠페인 내용을 저장하면서 SEND_FLAG를 20으로 업데이트
        Campaign campaign = new Campaign();
		campaign.setSchdlId(schdlId);

        String contents = params.get("contents");
        String filePath = "";
        try {
            filePath = saveMailContent(contents, campaign.getSchdlId());
        } catch(Exception e) {
            // 파일 생성 실패(파일 생성 중 에러 발생)
            return makeResultStr("fail", "create mail content file fail");
        }

        campaign.setSchdlName(params.get("schdlName"));
        campaign.setSubject(params.get("subject"));
        campaign.setSender(StringUtils.defaultString(params.get("sender"), ""));
        campaign.setSenderName(params.get("senderName"));
        campaign.setReserveDate(params.get("reserveDate"));
        campaign.setReserveTime(params.get("reserveTime"));
        campaign.setFilePath(filePath);
        campaign.setSendFlag("20");
		
		if (campaignService.updateSchdlSendFlag(campaign) > 0)
			return makeResultStr("success", "");
		else
			return makeResultStr("fail", "DB update error");
	}

    // 발송 예약 취소
    @ResponseBody
    @RequestMapping(value = {"/mail/send/campaign/setting/send"}, method = {RequestMethod.DELETE})
    public String campaignSendCancel(@RequestBody Map<String, String> params) {
        int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(params.get("schdlId"), "0"));
        if (schdlId < 1) {
            return makeResultStr("fail", "Not exist schedule id");
        }

        Campaign campaign = new Campaign();
        campaign.setSchdlId(schdlId);
        campaign.setSendFlag("12");

        if (campaignService.updateSchdlSendFlag(campaign) > 0)
            return makeResultStr("success", "");
        else
            return makeResultStr("fail", "DB update error");
    }

    @ResponseBody
    @RequestMapping(value = {"/mail/send/campaign/tgt/status"}, method = {RequestMethod.POST})
    public String checkTargetingStatus(@RequestBody Map<String, String> params) {
        // 타겟팅 상태 체크
        int schdlId = Integer.parseInt(StringUtils.defaultIfEmpty(params.get("schdlId"), "0"));
        if (schdlId < 1) {
            return makeResultStr("fail", "Not exist schedule id");
        }

        Campaign campaign = campaignService.selectTargetStatus(schdlId);

        if (campaign != null && campaign.getSendFlag() != null && !"".equals(campaign.getSendFlag()))
            return makeResultStr("success", campaign.getSendFlag()+"");
        else
            return makeResultStr("fail", "Get target status flag error");
    }

	private String makeResultStr(String result, String resultDesc) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("result", StringUtils.defaultIfEmpty(result, "fail"));
        resultJson.put("desc", StringUtils.defaultIfEmpty(resultDesc, "(empty)"));

        return resultJson.toString();
    }

    private String saveMailContent(String contents, int schdlId) throws Exception{
        String htmlContents = htmlHeader + contents + htmlFooter;

        String fileName = "C_CONTENT_" + schdlId + ".html";
        String filePath = htmlPath + "/" + fileName;

        File file = null;

        try {
            // 파일은 기존 파일명에 새로운 파일 내용을 덮어씀
            file = new File(filePath);
            FileWriter fw = new FileWriter(file);

            fw.write(htmlContents);
            fw.flush();
            fw.close();
        } catch (IOException ioe) {
            logger.error("fail - file io");
            ioe.printStackTrace();
        }

        return filePath;
    }

}
