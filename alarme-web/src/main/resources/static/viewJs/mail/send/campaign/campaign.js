(function($, campaign){

	campaign.init = function(){
        this.setEvent();
    };

    campaign.setEvent = function(){
        $('button[name="campaignSetting"]').on('click.setting', function() {
        	var schdlId = $(this).attr("schdlId");
        	location.href="/mail/send/campaign/setting?schdlId="+schdlId;
        });
        $('button[name="campaignDelete"]').on("click.setting", function() {
            if (confirm("캠페인을 삭제하시겠습니까?")) {
                var schdlId = $(this).attr("schdlId");
                param = {
                    "schdlId" : schdlId
                };
                $.ajax({
                    method: "delete",
                    url: "/mail/send/campaign/setting",
                    data: param,
                    success: function(data) {
                        var jData = JSON.parse(data);
                        if(jData.result == "success"){
                            alert("삭제되었습니다.");
                            location.href="/mail/send/campaign";
                        }else{
                            alert("삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                    }
                });
            }

        });
    };

    campaign.init();
})(jQuery, {});
