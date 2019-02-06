(function($, realtimeSetting){

    realtimeSetting.init = function(){
        CKEDITOR.replace('contents');
        this.setEvent();
        this.detailInit();
    };

    realtimeSetting.setEvent = function(){
        $('button[name="realtimeSave"]').on('click.setting', function(){
            if( entryData ){
                realtimeSetting.edit();
                return;
            }

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

    realtimeSetting.ConvertSystemSourcetoHtml = function(str){
        str = str.replace(/&lt;/g, "<");
        str = str.replace(/&gt;/g, ">");
        str = str.replace(/\n/g,"<br />");
        str = str.replace(/&nbsp;/g," ");
        str = str.replace(/&amp;/g,"&");
        return str;
    };

    realtimeSetting.detailInit = function(){
        if( entryData && realTiScdlId ){
            $(function() {
                $('#title').val(realTiSetTitle);
                $('button[name="realtimeSave"]').data('schdlId',realTiScdlId);
                if( realTiSetHtml ){
                    setTimeout(function() {
                        CKEDITOR.instances.contents.setData(realtimeSetting.ConvertSystemSourcetoHtml(realTiSetHtml.substring(realTiSetHtml.search('&lt;p&gt;'), realTiSetHtml.search('&lt;/body'))));
                        // $('iframe').contents().find('body').html(realtimeSetting.ConvertSystemSourcetoHtml(realTiSetHtml.substring(realTiSetHtml.search('&lt;p&gt;'), realTiSetHtml.search('&lt;/body'))));
                    }, 1000);
                }
            });
        }
    };

    realtimeSetting.edit = function(){
        if( !realTiScdlId ){
            return;
        }
        var params = { schdlId : realTiScdlId, filePathHtml : CKEDITOR.instances.contents.getData(),
                       schdlName :  $('#title').val()};

        var sCallBack = function(resultData){
            if( resultData && resultData.data ){
                if( resultData.data === 'SUCCESS'){
                    console.log('성공');
                    location.href='/mail/send/realtime';
                }
            }
        }

        alarmeCommon.ajaxCall('put','/mail/send/realtime/setting/save',JSON.stringify(params), null,null,sCallBack,null);
    }

    realtimeSetting.init();
})(jQuery, {});
