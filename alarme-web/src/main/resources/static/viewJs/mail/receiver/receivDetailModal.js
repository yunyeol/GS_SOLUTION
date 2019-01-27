(function($, receivDetailModal){
    receivDetailModal.field = {
        addrGrpId : null,
        currIdx : 0,
        editField : {
            editClickChk : false,
            editAddrMbrId : ''
        }
    }

    receivDetailModal.init = function(){
        this.setEvent();
    }

    receivDetailModal.setEvent = function(){
        if(receiverObj && receiverObj.dataTable){
            receiverObj.field.dataTable.off('click.receiverDetailModal').on('click.receiverDetailModal','button[name="receiverDetail"]',function(e){
                receivDetailModal.allReset();
                receivDetailModal.field.addrGrpId = $(e.currentTarget).data('idx');
                receivDetailModal.field.currIdx = 1;
                if ( !receivDetailModal.field.addrGrpId ) { return; }

                receivDetailModal.setPaging();
            });
        }

        $('#receivDetSearch').off('keyup.receiverDetKeyup').on('keyup.receiverDetKeyup', function(e){
            receivDetailModal.field.currIdx = 1;
            receivDetailModal.setPaging();
        });

        //테스트 확인
        $('#receivDetailAdd').off('click.receiverDetailModal').on('click.receiverDetailModal', function(e){
            if( receivDetailModal.validation() ){
                var params ={};
                params.address = $('#formEmail').val();
                params.name = $('#formName').val();
                params.data1 = $('#formTelnumber').val();
                params.addrGrpId = receivDetailModal.field.addrGrpId;

                //중복체크 확인 후 add
                var ssCallBack = function(resultData){
                    if(resultData && resultData.data === 'N'){
                        var sCallBack = function(resultData){
                            if(resultData && resultData.data === 'SUCCESS'){
                                receivDetailModal.setPaging();
                            }else{
                                alert('생성 실패');
                            }
                        }
                        alarmeCommon.ajaxCall('post','/mail/receiver/group/detail',JSON.stringify(params),null,null,sCallBack,null);
                    }else{
                        alert('추가하려는 데이터가 존재합니다. \r\n 데이터를 확인해 주시기 바랍니다.');
                    }
                }

                alarmeCommon.ajaxCall('get','/mail/receiver/group/detail/checkRow',params, null,null,ssCallBack,null);
            }
        });

        //테스트 확인
        $('#receivDetailEdit').off('click.receiverDetailModal').on('click.receiverDetailModal', function(e){
            if( receivDetailModal.field.editField.editClickChk ){
                if( receivDetailModal.validation() ){
                    if( !receivDetailModal.field.editField.editAddrMbrId ) {
                        console.log('is not defined mbrId');
                        return;
                    }

                    var params ={};
                    params.address = $('#formEmail').val();
                    params.name = $('#formName').val();
                    params.data1 = $('#formTelnumber').val();
                    params.addrGrpId = receivDetailModal.field.addrGrpId;
                    params.addrMbrId = receivDetailModal.field.editField.editAddrMbrId;

                    var ssCallBack = function(resultData){
                        if(resultData && resultData.data === 'N'){
                            var sCallBack = function(resultData){
                                if(resultData && resultData.data === 'SUCCESS'){
                                    receivDetailModal.setPaging();
                                }else{
                                    alert('수정 실패');
                                }
                            }
                            alarmeCommon.ajaxCall('put','/mail/receiver/group/detail',JSON.stringify(params),null,null,sCallBack,null);
                        }else{
                            alert('수정하려는 데이터가 존재합니다. \r\n 데이터를 확인해 주시기 바랍니다.');
                        }
                    }

                    alarmeCommon.ajaxCall('get','/mail/receiver/group/detail/checkRow',params, null,null,ssCallBack,null);
                }
            }else{
                alert('리스트중 하나를 선택하여 주시기 바랍니다.');
            }
        });

        $('#receivDetailReset').off('click.receiverDetailModal').on('click.receiverDetailModal', function(e){
            $('#formEmail').val('');
            $('#formName').val('');
            $('#formTelnumber').val('');
            receivDetailModal.field.editField.editClickChk = false;
            receivDetailModal.field.editField.editAddrMbrId = '';
        });

        $('#receiverDetailListTable tr td#delTd').off('click.receiverDetailModal').on('click.receiverDetailModal',function(e){
            e.stopPropagation();
            if( confirm('삭제하시겠습니까?') ){
                var addrMbrId = $(this).data('addrmbrid');
                if(!addrMbrId){
                    return;
                }

                var sCallBack = function(resultData){
                    if(resultData && resultData.data === 'SUCCESS'){
                        receivDetailModal.setPaging();
                    }else{
                        alert('삭제 실패');
                    }
                }

                alarmeCommon.ajaxCall('delete','/mail/receiver/group/detail/'+addrMbrId,null,null,null,sCallBack,null);
            }
        });

        $('#receiverDetailListTable tr').off('click.receiverDetailModal').on('click.receiverDetailModal',function(e){
            var elem = $(e.currentTarget);
            $('#formEmail').val(elem.data('email') || '');
            $('#formName').val(elem.data('name') || '');
            $('#formTelnumber').val(elem.data('telnumber') || '');
            receivDetailModal.field.editField.editClickChk = true;
            receivDetailModal.field.editField.editAddrMbrId = elem.data('addrmbrid') || '';
        });

        $('select[name="receiverDetailListTable_length"]').off('change.receivDetailSelected').on('change.receivDetailSelected',function(e){
            receivDetailModal.field.currIdx = 1;
            receivDetailModal.setPaging();
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
                        var evenHtml = '<tr role="row" class="even" data-addrGrpId="'+elem.addrGrpId+'" data-addrMbrId="'+elem.addrMbrId+'" data-name="'+elem.name+'" data-email="'+elem.address+'" data-telnumber="'+elem.data1+'">';
                        var oddHtml = '<tr role="row" class="odd" data-addrGrpId="'+elem.addrGrpId+'" data-addrMbrId="'+elem.addrMbrId+'" data-name="'+elem.name+'" data-email="'+elem.address+'" data-telnumber="'+elem.data1+'">';

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
                var showTxt = 'showing '+resultData.data.startShowRow+' to '+resultData.data.endShowRow+ ' of '+resultData.data.totCnt;
                $('#receiverDetailListTable_info').text(showTxt);
                receivDetailModal.setEvent();
            }
        }

        var params = {};
        params.selected = $('select[name="receiverDetailListTable_length"] option:selected').val() || '10';
        params.addrGrpId = receivDetailModal.field.addrGrpId;
        params.currIdx = receivDetailModal.field.currIdx;
        params.keyword = $('#receivDetSearch').val();

        alarmeCommon.ajaxCall('get','/mail/receiver/group/detail',params, null,null,sCallBack,null);
    }

    receivDetailModal.allReset = function(){
        this.field = {
            addrGrpId : null,
            currIdx : 0,
            editField : {
                editClickChk : false,
                editAddrMbrId : ''
            }
        }
        $('#formEmail').val('');
        $('#formName').val('');
        $('#formTelnumber').val('');
        $('#receivDetSearch').val('');
        $('select[name="receiverDetailListTable_length"] option').eq(0).prop('selected',true);
    }

    receivDetailModal.init();
})(jQuery, {});
