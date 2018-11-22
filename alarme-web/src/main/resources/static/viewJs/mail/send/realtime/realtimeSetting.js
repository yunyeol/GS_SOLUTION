(function($, realtimeSetting){

    realtimeSetting.init = function(){
        CKEDITOR.replace( 'contents');

        this.setEvent();
    };

    realtimeSetting.setEvent = function(){
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
