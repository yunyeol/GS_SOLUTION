var receiverObj = (function($, receivDetailModal){
    receivDetailModal.field = {
        addrGrpId : null,
        currIdx : 0
    }

    receivDetailModal.pagination;

    receivDetailModal.init = function(){
        this.setEvent();
    }

    receivDetailModal.setEvent = function(){
        if(receiverObj && receiverObj.dataTable){
            receiverObj.field.dataTable.off('click.receiverDetailModal').on('click.receiverDetailModal','button[name="receiverDetail"]',function(e){
                receivDetailModal.field.addrGrpId = $(e.currentTarget).data('idx');
                receivDetailModal.field.currIdx = 1;
                if ( !receivDetailModal.field.addrGrpId ) { return; }

                receivDetailModal.setPaging();
            });
        }
    }

    receivDetailModal.setPagination = function(totPage, visibPage, startPage){
        receivDetailModal.pagination = $('#pagination');
        receivDetailModal.pagination.twbsPagination('destroy');
        receivDetailModal.pagination.twbsPagination({
            totalPages: totPage,  // 전체 page블럭 수
            visiblePages: visibPage,  // 출력될 page 블럭수 상수로 지정해 줘도 되고, 변수로 지정해줘도 된다.
            startPage: startPage || 1,
            prev: "PREVIOUS",
            next: "NEXT",
            first: 'FIRST',
            last: 'LAST',
            initiateStartPageClick:false,
            onPageClick: function (event, page) {
                receivDetailModal.field.currIdx = page;
                receivDetailModal.setPaging();
            }
        });
    }

    receivDetailModal.setPaging = function(){
        var sCallBack = function(resultData) {
            //여기부터 진행
            if( resultData && resultData.data ){
                var contentList = resultData.data.contentList;
                if( contentList && contentList.length > 0){
                    var $html = '';
                    contentList.forEach(function(elem, index){
                        elem.createdDt = (elem.createdDt) ? moment(elem.createdDt).format('YYYY.MM.DD') : '';
                        elem.modifiedDt = (elem.modifiedDt) ? moment(elem.modifiedDt).format('YYYY.MM.DD') : '';
                        $html += (index%2==0) ? '<tr role="row" class="even">':'<tr role="row" class="odd">';
                        $html += '<td class="text-center">'+elem.addrGrpName+'</td>';
                        $html += '<td class="text-center">'+elem.address+'</td>';
                        $html += '<td class="text-center">'+elem.name+'</td>';
                        $html += '<td class="text-center">'+elem.data1+'</td>';
                        $html += '<td class="text-center">'+elem.createdDt+'</td>';
                        $html += '<td class="text-center">'+elem.modifiedDt+'</td>';
                        $html += '<td class="text-center">X</td>';
                        $html += '</tr>';
                    });
                    $('#receiverDetailListTable tbody').html($html);
                }

                receivDetailModal.setPagination(resultData.data.totPage, 5, receivDetailModal.field.currIdx );
            }
        }

        alarmeCommon.ajaxCall('get','/mail/receiver/group/detail',receivDetailModal.field, null,null,sCallBack,null);
    }

    receivDetailModal.init();
})(jQuery, {});
