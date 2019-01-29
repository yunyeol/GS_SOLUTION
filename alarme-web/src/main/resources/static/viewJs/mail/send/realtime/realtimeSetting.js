(function($, realtimeSetting){

    realtimeSetting.init = function(){
        this.setEvent();
        console.log('setEvent go');
        // CKEDITOR.replace('contents');
    };

    realtimeSetting.setEvent = function(){
        $(function() {
            $('.bootstrap-switch-handle-on, .bootstrap-switch-handle-off, .bootstrap-switch-label').on('click',function(){
                console.log($(this).siblings().filter('input').is(':checked'));
                var chkbox = $(this).siblings().filter('input');
                console.log(chkbox);
                if( chkbox[0] ){
                    var $schdlId = chkbox.data('schdlid') || '';
                    var $activeYn = chkbox.is(':checked') ? 'Y' : 'N';
                    var params = { schdlId : $schdlId, activeYn : $activeYn };

                    if( !$schdlId ){
                        return;
                    }

                    var sCallBack = function(resultData){
                        if( resultData && resultData.data ){
                            if( resultData.data === 'SUCCESS'){
                                console.log('성공');
                            }
                        }
                        console.log('테스트');
                    }

                    alarmeCommon.ajaxCall('put','/mail/send/realtime/setting/activeYn',JSON.stringify(params), null,null,sCallBack,null);
                }
            });
        });

        $('button[name="realtimeSave"]').on('click.setting', function(){
            var data = {
                "title" : $('#title').val(),
                "contents" : CKEDITOR.instances.contents.getData(),
                "sender" : $('#loginId').val(),
                "send_gubun" : "M",
                "send_type" : "R_M"
            };

            if(confirm("저장하시겠습니까?") == true){
                $.ajax({
                    method: "post",
                    url: "/mail/send/realtime/setting/save",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    //dataType: "json",
                    success: function(data) {
                        if(data.code == "success"){

                        }else{
                            location.href="/mail/send/realtime"
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                    }
                });
            }else{
                return;
            }
        });
    };

    realtimeSetting.init();
})(jQuery, {});
