(function($, realtime){

    realtime.field = {
        currIdx : 1
    }

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


            $('.main-panel').scroll(function(){
                var psRailY = $('.main-panel .ps__rail-y');
                var docHeight = Math.floor( psRailY.height() );
                var scrollHeight = Math.floor( psRailY.children().height() ) ;
                var scrollTop = Math.floor( psRailY.children().offset().top );

                // console.log('docHeight :'+docHeight+'/ scrollHeight: '+scrollHeight+'/ scrollTop:'+scrollTop);
                if( realtime.scrollCalc(docHeight, scrollHeight, scrollTop) ){
                    var totCnt = $('#realtimeHiddeEle').data('totalcnt');
                    if( totCnt && alarmeCommon.checkPaging(realtime.field.currIdx+1,Number(totCnt),10) ){
                        var params = {};
                        params.currIdx = ++realtime.field.currIdx;
                        params.isAjax = true;

                        var sCallback = function(resultData){
                            var resultDom = $('<div></div>>').html(resultData);
                            $('#realtimeHiddeEle').data('totalcnt',resultDom.find('#realtimeHiddeEle').data('totalcnt'));
                            $('#contentRow').append(resultDom.find('div.card'));
                        }

                        alarmeCommon.ajaxCall('get','/mail/send/realtime',params,'html',null,sCallback,null);
                    }
                }

            })
        });
    };

    realtime.scrollCalc = function(docHeight, scrollHeight, scrollTop){
        var sumScrollVal = scrollHeight + scrollTop;
        if( docHeight === sumScrollVal ){
            return true;
        }
        if( docHeight - sumScrollVal === 1 || docHeight - sumScrollVal === -1){
            return true;
        }
        if( sumScrollVal - sumScrollVal === 1 || sumScrollVal - sumScrollVal === -1){
            return true;
        }
        return false;
    }

    realtime.init();
})(jQuery, {});
