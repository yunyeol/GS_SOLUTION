var receiverObj = (function($, receivDetailModal){
    receivDetailModal.field = {
        addrGrpId : null,
        currIdx : 0,
        editClickChk : false
    }

    window.pagination = receivDetailModal.pagination;

    receivDetailModal.init = function(){
        this.setEvent();
        console.log('init');
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

        //테스트 확인
        $('#receivDetailAdd').off('click.receiverModal').on('click.receiverModal', function(){
            if( receiverObj.validation() ){
                console.log('ajax go');
                
                //중복체크 확인
                
                var params ={};
                params.address = $('#formEmail').val();
                params.name = $('#formName').val();
                params.data1 = $('#formTelnumber').val();
                var sCallBack = function(){
                    receivDetailModal.setPaging();
                }

                alarmeCommon.ajaxCall('post','/mail/receiver/group',JSON.stringify(params),null,null,sCallBack,null);
            }
        });

        //테스트 확인
        $('#receivDetailEdit').off('click.receiverModal').on('click.receiverModal', function(){
            if( receivDetailModal.field.editClickChk ){
                if( receivDetailModal.validation() ){
                    var params ={};
                    params.address = $('#formEmail').val();
                    params.name = $('#formName').val();
                    params.data1 = $('#formTelnumber').val();
                    var sCallBack = function(){
                        receivDetailModal.setPaging();
                    }

                    alarmeCommon.ajaxCall('put','/mail/receiver/group/detail',JSON.stringify(params),null,null,sCallBack,null);
                }
            }
        });

        $('#receivDetailReset').off('click.receiverModal').on('click.receiverModal', function(){
            $('#formEmail').val('');
            $('#formName').val('');
            $('#formTelnumber').val('');
        });

        $('#receiverDetailListTable tr td#delTd').off('click.receiverDetailModal').on('click.receiverDetailModal',function(e){
            if( confirm('삭제하시겠습니까?') ){
                var sCallBack = function(){
                    var addrMbrId = $(this).data('addrMbrId');
                    //진행예정

                    var sCallBack = function(){
                        receivDetailModal.setPaging();
                    }

                    alarmeCommon.ajaxCall('delete','/mail/receiver/group//detail'+addrMbrId,null,null,null,sCallBack,null);
                }
            }
        });

        $('#receiverDetailListTable tr').off('click.receiverDetailModal').on('click.receiverDetailModal',function(e){ssss
            var params = {};
            params.addrGrpId = $(this).data('addrgrpid') || null;
            params.addrMbrId = $(this).data('addrmbrid') || null;
            params.name = $(this).data('name') || '';
            params.address = $(this).data('email') || '';
            params.data1 = $(this).data('telnumber') || '';

            $('#formEmail').val($(this).data('email'));
            $('#formName').val($(this).data('name'));
            $('#formTelnumber').val($(this).data('telnumber'));
            receivDetailModal.field.editClickChk = true;
        });

    }

    receivDetailModal.validation = function(){

        if( !$('#formEmail').val() ){
            alert('이메일을 입력해 주시기 바랍니다.');
            return false;
        }
        if( !$('#formName').val() ){
            alert('이름을 입력해 주시기 바랍니다.');
            return false;
        }
        if( !$('#formTelnumber').val() ){
            alert('핸드폰을 입력해 주시기 바랍니다.');
            return false;
        }

        return true;
    }

    receivDetailModal.setPagination = function(totPage, visibPage, startPage){
        receivDetailModal.pagination = $('#pagination');
        receivDetailModal.pagination.twbsPagination('destroy');
        receivDetailModal.pagination.twbsPagination({
            totalPages: (totPage > 1) ? totPage : 1,  // 전체 page블럭 수
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
                var $html = '';
                if( contentList && contentList.length > 0){
                    contentList.forEach(function(elem, index){
                        elem.createdDt = (elem.createdDt) ? moment(elem.createdDt).format('YYYY.MM.DD') : '';
                        elem.modifiedDt = (elem.modifiedDt) ? moment(elem.modifiedDt).format('YYYY.MM.DD') : '';
                        var evenHtml = '<tr role="row" class="even" data-addrGrpId="'+elem.addrGrpId+'" data-addrMbrId="'+elem.addrMbrId+'" data-name="'+elem.name+'" data-email="'+elem.address+'" data-telnumber="'+elem.address+'">';
                        var oddHtml = '<tr role="row" class="odd" data-addrGrpId="'+elem.addrGrpId+'" data-addrMbrId="'+elem.addrMbrId+'" data-name="'+elem.name+'" data-email="'+elem.address+'" data-telnumber="'+elem.address+'">';

                        $html += (index%2==0) ? evenHtml:oddHtml;
                        $html += '<td name="rowTd" class="text-center">'+elem.addrGrpName+'</td>';
                        $html += '<td name="rowTd" class="text-center">'+elem.address+'</td>';
                        $html += '<td name="rowTd" class="text-center">'+elem.name+'</td>';
                        $html += '<td name="rowTd" class="text-center">'+elem.data1+'</td>';
                        $html += '<td name="rowTd" class="text-center">'+elem.createdDt+'</td>';
                        $html += '<td name="rowTd" class="text-center">'+elem.modifiedDt+'</td>';
                        $html += '<td id="delTd" class="text-center" data-addrGrpId="'+elem.addrGrpId+'" data-addrMbrId="'+elem.addrMbrId+'">X</td>';
                        $html += '</tr>';
                    });
                }else{
                    $html = '<td class="text-center" colspan="7">데이터가 존재하지 않습니다.</td>';
                }
                $('#receiverDetailListTable tbody').html($html);

                receivDetailModal.setPagination(resultData.data.totPage, 5, receivDetailModal.field.currIdx );
                receivDetailModal.setEvent();
            }
        }

        alarmeCommon.ajaxCall('get','/mail/receiver/group/detail',receivDetailModal.field, null,null,sCallBack,null);
    }

    receivDetailModal.init();
})(jQuery, {});
