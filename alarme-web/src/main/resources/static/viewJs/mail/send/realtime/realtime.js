(function($, realtime){

    realtime.init = function(){
        this.setEvent();
    };

    realtime.setEvent = function(){
        $('button[name="realtimeDettaSetting"]').off('click.settingDetta').on('click.settingDetta',function(){
            var schdId = $(this).data('schdlid') || '';

            if( !schdId ){
                return;
            }

            location.href='/mail/send/realtime/setting?viewType=DETAIL&schdlId='+schdId;
        });
        $('button[name="realTimeDelSetting"]').off('click.settingDel').on('click.settingDel',function(){
            var schdId = $(this).data('schdlid') || '';

            if( !schdId ){
                return;
            }

            if( confirm('삭제하시겠습니까?') ){
                var sCallBack = function(resultData){
                    if( resultData && resultData.data ){
                        if( resultData.data === 'SUCCESS'){
                            location.href='/mail/send/realtime';
                        }
                    }
                }

                alarmeCommon.ajaxCall('delete','/mail/send/realtime/'+schdId,null,null,null,sCallBack,null);
            }
        });
        $(function() {
            $('.bootstrap-switch-handle-on, .bootstrap-switch-handle-off, .bootstrap-switch-label').off('click.settingOnOff').on('click.settingOnOff',function(){
                var chkbox = $(this).siblings().filter('input');
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
                    }

                    alarmeCommon.ajaxCall('put','/mail/send/realtime/setting/activeYn',JSON.stringify(params), null,null,sCallBack,null);
                }
            });
        });
    };

    realtime.init();
})(jQuery, {});
