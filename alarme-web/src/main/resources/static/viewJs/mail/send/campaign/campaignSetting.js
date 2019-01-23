(function($, campaignSetting){
	
	var $datepicker = $(".datepicker");
    var $timepicker = $(".timepicker");

	campaignSetting.init = function(){
        CKEDITOR.replace('contents');
        
        if (!$("#sendType").val())
        	$("#sendType").val("C_F");
        
        if (!$("#sendFlag").val())
        	$("#sendFlag").val("00");
        
        if (!$("#schdlId").val())
        	$("#schdlId").val("0");
        
        initDateTimePicker();
        
        this.setEvent();
    };

    campaignSetting.setEvent = function(){
    	// 버튼 이벤트
    	// 캠페인 저장
        $('button[name="campaignSave"]').on('click.setting', function(){
            if(confirm("저장하시겠습니까?")) {
            	saveCampaign();
            }else{
                return;
            }
        });
        
        // 타겟팅 시작
        $("#startTgt").on('click.setting', function() {
        	if(confirm("타겟팅을 시작하시겠습니까?")) {
        		// 타겟팅 시작
        		alert("타겟팅을 시작합니다.");
        	}
        });
        $('a[name="tgtTab"]').on('click.setting', function() {
        	$("#targetType").val($(this).attr("type"));
        });
        
        // 타겟팅 파일 업로드
        $("#uploadTgtFile").on('click.setting', function() {
        	alert($("#schdlId").val());
        	if(confirm("파일을 업로드 하시겠습니까?")) {
        		
        		if(!$("#schdlId").val() || $("#schdlId").val() == 0) {
        			alert("캠페인 저장");
//        			saveCampaign();
        		}
        		
        		var data = new FormData($("#targetFileForm")[0]);
        		data.append("schdlId", $("#schdlId").val());
        		
        		$.ajax({
        			type: "post",
        			url: "/mail/send/campaign/setting/tgt/fileUpload",
        			data: data,
        			processData: false,
        			contentType: false,
        			cache: false,
        			success: function(data) {
        				alert("파일 업로드 성공");
        			},
        			error: function(e) {
        				alert("파일 업로드 실패");
        			}
        		});
        	}
        });
    };
    
    function saveCampaign() {
    	var data = {
    		"schdlId" : $('#schdlId').val(),
    		"sendType" : $('#sendType').val(),
    		"sendFlag" : $("#sendFlag").val(),
            "schdlName" : $('#schdlName').val(),
            "subject" : $('#subject').val(),
            "sender" : $('#sender').val(),
            "senderName" : $('#senderName').val(),
            "contents" : CKEDITOR.instances.contents.getData(),
            "reserveDate" : $("#reserveDate").val(),
            "reserveTime" : $("#reserveTime").val()
        };
    	
        $.ajax({
            method: "post",
            url: "/mail/send/campaign/setting/save",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
            	var jData = JSON.parse(data);
            	alert("data.result : " + jData.result);
				if(jData.result == "success"){
					alert("저장되었습니다.");
                }else{
                	alert("저장 중 오류가 발생했습니다. 다시 시도해주세요.\n(" + data.desc + ")");
//                    location.href="/mail/send/campaign"
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        });
    }
    
    function initDateTimePicker() {
        0 != $datetimepicker.length && $datetimepicker.datetimepicker({
            icons: {
                time: "tim-icons icon-watch-time",
                date: "tim-icons icon-calendar-60",
                up: "fa fa-chevron-up",
                down: "fa fa-chevron-down",
                previous: "tim-icons icon-minimal-left",
                next: "tim-icons icon-minimal-right",
                today: "fa fa-screenshot",
                clear: "fa fa-trash",
                close: "fa fa-remove"
            }
        }), 0 != $datepicker.length && $datepicker.datetimepicker({
            format: "YYYY/MM/DD",
            icons: {
                time: "tim-icons icon-watch-time",
                date: "tim-icons icon-calendar-60",
                up: "fa fa-chevron-up",
                down: "fa fa-chevron-down",
                previous: "tim-icons icon-minimal-left",
                next: "tim-icons icon-minimal-right",
                today: "fa fa-screenshot",
                clear: "fa fa-trash",
                close: "fa fa-remove"
            }
        }), 0 != $timepicker.length && $timepicker.datetimepicker({
            format: "HH:mm",
            icons: {
                time: "tim-icons icon-watch-time",
                date: "tim-icons icon-calendar-60",
                up: "fa fa-chevron-up",
                down: "fa fa-chevron-down",
                previous: "tim-icons icon-minimal-left",
                next: "tim-icons icon-minimal-right",
                today: "fa fa-screenshot",
                clear: "fa fa-trash",
                close: "fa fa-remove"
            }
        })
    }

    campaignSetting.init();
})(jQuery, {});
