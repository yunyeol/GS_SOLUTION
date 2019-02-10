(function($, campaignSetting){
	
	var $datepicker = $(".datepicker");
    var $timepicker = $(".timepicker");

    var checkTargetingStatus;

	campaignSetting.init = function(){
        CKEDITOR.replace('contents');

        if ("0" == $("#schdlId").val()) {
            var currentDate = new Date();
            var currentDateString = currentDate.getFullYear() + "/"
                + ((1 + currentDate.getMonth()) < 10 ? "0" + (1 + currentDate.getMonth()) : currentDate.getMonth()) + "/"
                + (currentDate.getDate() < 10 ? "0" + currentDate.getDate() : currentDate.getDate());
            var currentTimeString = (currentDate.getHours() < 10 ? "0" + currentDate.getHours() : currentDate.getHours()) + ":"
                + (currentDate.getMinutes() < 10 ? "0" + currentDate.getMinutes() : currentDate.getMinutes());

            $("#reserveDate").val(currentDateString);
            $("#reserveTime").val(currentTimeString);
        }


        
        initDateTimePicker();

        initCampaignSetting();
        
        this.setEvent();
    };

    campaignSetting.setEvent = function(){
    	// 버튼 이벤트
    	// 캠페인 저장
        $("#campaignSave").on('click.setting', function(){
            if(confirm("저장하시겠습니까?")) {
                // 유효성 체크(필수 입력 필드 확인)
                if (!validationCampaign("save")) {
                    return;
                }
            	saveCampaign();
            }else{
                return;
            }
        });

        // 캠페인 메일 발송 예약
        $("#campaignSend").on("click.setting", function() {
            // 유효성 체크(필수 입력 필드 확인)
            if (!validationCampaign("send")) {
                return;
            }
            if (confirm("발송 예약하시겠습니까?")) {
                // 발송 요청 - 다시 한 번 데이터를 저장하기 위해 폼 데이터를 모두 전달
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
                    url: "/mail/send/campaign/setting/send",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    success: function(data) {
                        var jData = JSON.parse(data);
                        if(jData.result == "success"){
                            alert("발송 예약되었습니다.");
                            $("#sendFlag").val("20");
                            $("#campaignSave").hide();
                            $("#campaignSend").hide();
                            $("#campaignSendCancel").show();
                        }else{
                            alert("발송 예약 중 오류가 발생했습니다. 다시 시도해주세요.");
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                    }
                });
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
        				alert("파일 업로드 성공");
        			},
        			error: function(e) {
        				alert("파일 업로드 실패");
        			}
        		});
        	}
        });

        // 캠페인 발송 예약 취소 버튼 클릭 이벤트
        $("#campaignSendCancel").on("click.setting", function() {
            // 취소 버튼 클릭 시점에서 발송 시각이 지나지 않은 경우에만 취소 가능
            var currentDate = new Date();
            if (currentDate > getReserveDateObj()) {
                alert("이미 발송 예약 시각이 지나 취소할 수 없습니다. 관리자에게 문의하세요.");
                return;
            }

            // 발송 예약 취소 - 타겟팅이 정상 완료되었다는 가정 하에 상태 코드를 12로 업데이트
            if (confirm("발송 예약을 취소하시겠습니까?")) {
                var data = {
                    "schdlId" : $('#schdlId').val()
                };

                $.ajax({
                    method: "delete",
                    url: "/mail/send/campaign/setting/send",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    success: function(data) {
                        var jData = JSON.parse(data);
                        if(jData.result == "success"){
                            alert("예약 취소되었습니다.");
                            $("#sendFlag").val("12");
                            $("#campaignSave").show();
                            $("#campaignSend").show();
                            $("#campaignSendCancel").hide();
                        }else{
                            alert("예약 취소 중 오류가 발생했습니다. 관리자에게 문의하세요.");
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                    }
                });
            }
        });
    };

    // 페이지 진입 시 SEND_FLAG에 따른 초기화
    function initCampaignSetting() {
        var sendFlag = $("#sendFlag").val();

        // 타겟팅 중
        if ("11" == sendFlag) {
            checkTargetingStatus = setInterval(function() {
                getTargetingStatus()
            }, 1000);
            $("#startTgt").attr("disabled", "disabled");
            $("#targetingSettingDiv").hide();
            $("#targetingStatusDiv").show();
            $("#targetingStatusDiv").html("타겟팅 진행 중입니다.");
        }
        // 타겟팅 완료
        else if ("12" == sendFlag) {
            $("#startTgt").hide();
            $("#targetingSettingDiv").hide();
            $("#targetingStatusDiv").show();
            $("#targetingStatusDiv").html("타겟팅이 완료되었습니다.");
        }
        // 발송 예약(발송 예약 취소 버튼을 제외한 제어 버튼 숨김)
        else if ("20" == sendFlag) {
            $("#startTgt").hide();
            $("#targetingSettingDiv").hide();
            $("#targetingStatusDiv").show();
            $("#targetingStatusDiv").html("타겟팅이 완료되었습니다.");
            $("#campaignSave").hide();
            $("#campaignSend").hide();
            $("#campaignSendCancel").show();
        }
        // 발송 이후(모든 제어 버튼 숨김)
        else if (sendFlag*1 >= 30) {
            $("#startTgt").hide();
            $("#targetingSettingDiv").hide();
            $("#targetingStatusDiv").show();
            $("#targetingStatusDiv").html("타겟팅이 완료되었습니다.");
            $("#campaignSave").hide();
            $("#campaignSend").hide();
            $("#campaignSendCancel").hide();
        }
    }

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

        // 파일을 선택하지 않고 업로드 요청을 한 경우
        if (!$("#targetFile").val()) {
            alert("타겟팅 대상 파일을 선택하세요.");
            return;
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
            data.addressGrpId = $("#addressGrpList option:selected").val();
        }

        $.ajax({
            method: "post",
            url: "/mail/send/campaign/setting/tgt/start",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var jData = JSON.parse(data);
                if(jData.result == "success"){
                    $("#startTgt").attr("disabled", "disabled");
                    $("#targetingSettingDiv").hide();
                    $("#targetingStatusDiv").show();
                    $("#targetingStatusDiv").html("타겟팅 진행 중입니다.");
                    alert("타겟팅이 시작되었습니다.");
                    $("#sendFlag").val("11");
                    checkTargetingStatus = setInterval(function() {
                        getTargetingStatus()
                    }, 2000);
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
                    var targetStatusFlag = jData.desc;
                    // 12 - 타겟팅 완료
                    if (targetStatusFlag == "12") {
                        clearInterval(checkTargetingStatus);
                        alert("타겟팅이 완료되었습니다.");
                        $("#sendFlag").val("12");
                        $("#targetingStatusDiv").html("타겟팅이 완료되었습니다.");
                    }
                    // 13 - 타겟팅 실패
                    else if (targetStatusFlag == "13") {
                        clearInterval(checkTargetingStatus);
                        alert("타겟팅을 실패했습니다. 다시 진행해주세요.");
                        $("#sendFlag").val("13");
                        $("#startTgt").removeAttr("disabled");
                        $("#targetingStatusDiv").hide();
                        $("#targetingStatusDiv").html("");
                        $("#targetingSettingDiv").show();
                    }
                    // 11 - 타겟팅 진행 중
                } else {
                    clearInterval(checkTargetingStatus);
                    alert("타겟팅 상태 조회를 실패했습니다.");
                    $("#targetingStatusDiv").html("타겟팅 상태 조회를 실패했습니다. 타겟팅이 진행 중이거나 완료 되었을 수 있습니다.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        })
    }

    // 저장 혹은 발송 요청 전 유효성 체크
    function validationCampaign(chkType) {
        if (!$("#schdlName").val()) {
            alert("캠페인명을 입력하세요.");
            return;
        }
        if (!$("#subject").val()) {
            alert("메일 제목을 입력하세요.");
            return;
        }
        if (!$("#sender").val()) {
            if(!confirm("발신자 이메일을 입력하지 않을 경우, 회원 정보에 저장된 메일 주소를 사용합니다. 계속 진행하시겠습니까?")) {
                return;
            }
        }

        // 발송 요청 전 유효성 체크를 하는 경우 타겟팅 여부와 예약시간을 추가로 체크
        if ("send" == chkType) {
            // 타겟팅 완료 여부 체크
            if ("12" != $("#sendFlag").val()) {
                alert("타겟팅을 먼저 완료하세요.");
                return false;
            }

            // 발송 예약시간 체크
            var currentDate = new Date();
            if (currentDate > getReserveDateObj()) {
                alert("발송 예약시간은 현재보다 과거로 설정할 수 없습니다.");
                return false;
            }
        }

        return true;
    }

    // 발송 예약 시각 폼에 세팅된 값을 Date 객체로 만들어 반환(시간 체크용)
    function getReserveDateObj() {
        var reserveDate = new Date();
        reserveDate.setFullYear($("#reserveDate").val().substring(0, 4)*1,
            ($("#reserveDate").val().substring(5, 7)*1)-1,
            $("#reserveDate").val().substring(8, 10)*1);
        reserveDate.setHours($("#reserveTime").val().substring(0, 2)*1,
            $("#reserveTime").val().substring(3, 5)*1,
            0,
            0);

        return reserveDate;
    }


    // DateTimePicker 초기화
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
