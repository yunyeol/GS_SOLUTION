(function($, receiverModal){
    receiverModal.field = {
        addrGrpId : null
    }

    receiverModal.init = function(){
        if(receiverObj && receiverObj.validation) { receiverObj.validation('#receiverEditValidation'); }
        this.setEvent();
    }
    receiverModal.setEvent = function(){
        if(receiverObj && receiverObj.dataTable){
            receiverObj.field.dataTable.off('click.receiverModal').on('click.receiverModal','button[name="receiverEdit"]',function(e){
                receiverModal.field.addrGrpId = $(e.currentTarget).data('idx');
                var addrGrpName = $(e.currentTarget).data('grpname');
                $('input[name="receivEditGrpName"]').val(addrGrpName);
            });
        }

        $('#receivModEditButt').off('click.receiverModal').on('click.receiverModal', function(){
            var addrGrpName = $('input[name="receivEditGrpName"]').val();
            var form = $('#receiverEditValidation');

            if( form.valid() ){
                if( !receiverModal.field.addrGrpId ) {
                    console.log('수정 불가');
                    return;
                }

                var params = {};
                params.addrGrpId = receiverModal.field.addrGrpId;
                params.addrGrpName = addrGrpName;

                var sCallBack = function(resultData) {
                    if( resultData && resultData.data ){
                        receiverObj.field.dataTable = receiverObj.dataTable();
                    }else{
                        alert('그룹수정이 실패하였습니다.');
                    }
                }

                alarmeCommon.ajaxCall('put','/mail/receiver/group',JSON.stringify(params),null,null,sCallBack,null);
            }
        });
    }
    receiverModal.init();
})(jQuery, {});
