(function($, campaignSetting){
	
	var $datepicker = $(".datepicker");
    var $timepicker = $(".timepicker");

    var checkTargetingStatus;

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

        console.log("sendFlag : " + $("#sendFlag").val());
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
                // 파일 타겟팅인 경우 파일 업로드를 먼저 수행한다.
                if ("C_F" == $("#sendType").val()) {
                    uploadTargetFile();
                } else {    // 주소록 타겟팅인 경우 바로 타겟팅 시작을 요청한다.
                    startTargeting();
                }
        	}
        });
        $('a[name="tgtTab"]').on('click.setting', function() {
        	$("#sendType").val($(this).attr("type"));
        });
        
        // 타겟팅 파일 업로드
        $("#uploadTgtFile").on('click.setting', function() {
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

    // 캠페인 저장
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
				if(jData.result == "success"){
				    $("#schdlId").val(jData.desc);
					alert("저장되었습니다.");
                }else{
                    alert("저장 중 오류가 발생했습니다. 다시 시도해주세요.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        });
    }

    // 타겟팅 파일 업로드 요청 - 업로드 성공 시 바로 타겟팅 시작 요청
    function uploadTargetFile() {
        // 저장되지 않은 캠페인일 경우 저장 먼저 수행 - 로직 수정 필요(순차 처리)
        if(!$("#schdlId").val() || $("#schdlId").val() == 0) {
       	    //saveCampaign();
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
                //alert("파일 업로드 성공");
                startTargeting();
            },
            error: function(e) {
                alert("파일 업로드 실패");
            }
        });
    }


    // 타겟팅 요청 - 파일 타겟팅의 경우 파일 업로드 이후 수행, 주소록(DB) 타겟팅의 경우 주소록 선택 이후 수행
    function startTargeting() {
        var data = {
            "schdlId" : $("#schdlId").val(),
            "sendType" : $("#sendType").val()
        };

        // 타겟팅 타입이 DB 타겟팅이라면, 주소록 시퀀스 번호를 넘긴다.
        if ("C_D" == $("#sendType").val()) {
            //data.push("addressGrpId", $("#addressGrpList option:selected").val());
            data.addressGrpId = $("#addressGrpList option:selected").val();
        }
        console.log("targeting start");
        $.ajax({
            method: "post",
            url: "/mail/send/campaign/setting/tgt/start",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var jData = JSON.parse(data);
                if(jData.result == "success"){
                    alert("타겟팅이 시작되었습니다.");
                    checkTargetingStatus = setInterval(getTargetingStatus(), 1000);
                }else{
                    alert("타겟팅 시작 요청 중 오류가 발생했습니다. 다시 시도해주세요.\n(" + data.desc + ")");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        });
    }

    function getTargetingStatus() {
        console.log("getTargetingStatus");
        var data = {
            "schdlId" : $("#schdlId").val()
        };

        $.ajax({
            method: "post",
            url: "/mail/send/campaign/tgt/status",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var jData = JSON.parse(data);
                if(jData.result == "success") {
                    var targetStatusFlag = jData.resultDesc;
                    if (targetStatusFlag == "12") {
                        alert("타겟팅이 완료되었습니다.");
                        clearInterval(checkTargetingStatus);
                    } else if (targetStatusFlag == "13") {
                        alert("타겟팅이 실패되었습니다.");
                        clearInterval(checkTargetingStatus);
                    }
                    // targetStatusFlag == "11" : 진행 중
                    else {
                        console.log("타겟팅 진행 중");
                    }
                } else {
                    alert("타겟팅 상태 조회를 실패했습니다.");
                    clearInterval(checkTargetingStatus);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        })
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
        });
    }

    campaignSetting.init();
})(jQuery, {});
