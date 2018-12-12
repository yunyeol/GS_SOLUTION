(function($, receiverModal){
    receiverModal.init = function(){
        this.setEvent();
    }
    receiverModal.setEvent = function(){
        if(receiverObj && receiverObj.dataTable){
            receiverObj.field.dataTable.off('click.receiver').on('click.receiver','button[name="receiverEdit"]',function(e){
                var addrGrpId = $(e.currentTarget).data('idx');
                var addrGrpName = $(e.currentTarget).data('dataGrpName');
                $('#receiverAddrGrpId').val(addrGrpId)
                $('input[name="receivEditGrpName"]').val(addrGrpName);
            });
        }
    }
    receiverModal.init();
})(jQuery, {});
