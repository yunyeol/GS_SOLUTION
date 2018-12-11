(function($, receiver){

    receiver.field = {
        grpNameChk : false,
        dataTable : undefined
    }
    receiver.init = function(){
        receiver.field.dataTable = this.dataTable();
        this.validation();
        this.setEvent();
    }

    receiver.setEvent = function(){
        $('#dupUsersBtn').off('click.receiver').on('click.receiver',function(){
            var grpName = $('#receiverForm input.form-control').val();

            if( !grpName ) { alert('그룹이름을 입력하여 주시기 바랍니다.'); return;};

            var params = {};
            params.addrGrpName = grpName;

            var sCallBack = function(resultData) {
                receiver.field.grpNameChk = ( resultData && resultData.data === 'N' ) ? true : false;
                if( receiver.field.grpNameChk ){
                    alert('그룹이름 : '+grpName+'은 사용 가능합니다.');
                    $('#receiverForm input.form-control').attr( 'disabled', true );
                }else{
                    alert('그룹이름 : '+grpName+'은 존재합니다. 사용 불가');
                }
            }

            alarmeCommon.ajaxCall('get','/mail/receiver/group/checkname',params,null,null,sCallBack,null);
        });

        $('button[type="submit"]').off('click.receiver').on('click.receiver',function(){
            var form = $('#receiverForm');
            var grpInputForm = $('#receiverForm input.form-control');
            var params = {};
            params.addrGrpName = grpInputForm.val();
            params.loginId = $('input[type=hidden]#loginId').val();

            var sCallBack = function(resultData) {
                if(resultData && resultData.data === 'SUCCESS'){
                    grpInputForm.val('').attr('disabled', false);
                    receiver.field.grpNameChk = false;
                    receiver.field.dataTable = receiver.dataTable();
                }else{
                    alert('그룹생성이 실패하였습니다.');
                }
            }

            if( form.valid() ){
                if( !receiver.field.grpNameChk ){
                    alert('중복체크 여부 확인을 해주시기 바랍니다.');
                    return;
                }

                alarmeCommon.ajaxCall('post','/mail/receiver/group',JSON.stringify(params),null,null,sCallBack,null);
            }else{
                alert('그룹이름을 입력해 주시기 바랍니다.');
            }
        });

        $('#init').off('click.receiver').on('click.receiver',function(){
            $('#receiverForm input.form-control').val('').attr('disabled', false);
            receiver.field.grpNameChk = false;
            receiver.validation();
        });

        if(receiver.field.dataTable){
            receiver.field.dataTable.off('click.receiver').on('click.receiver','.delete.tim-icons',function(e){
                if( confirm("삭제하시겠습니까?") ){
                    var addrGrpId = $(e.currentTarget).data('idx');
                    if( !addrGrpId && typeof addrGrpId !== 'number' ){
                        console.log('삭제 불가');
                        return;
                    }

                    var sCallBack = function(resultData){
                        if( resultData && resultData.data ){
                            receiver.field.dataTable = receiver.dataTable();
                        }else{
                            alert('그룹삭제가 실패하였습니다.');
                        }
                    }

                    alarmeCommon.ajaxCall('delete','/mail/receiver/group/'+addrGrpId,null,null,null,sCallBack,null);
                }
            });
        }
    }

    receiver.validation = function(){
        $('#receiverForm').validate({
            highlight: function(element) {
                $(element).closest('.form-group').removeClass('has-success').addClass('has-danger');
                $(element).closest('.form-check').removeClass('has-success').addClass('has-danger');
            },
            success: function(element) {
                $(element).closest('.form-group').removeClass('has-danger').addClass('has-success');
                $(element).closest('.form-check').removeClass('has-danger').addClass('has-success');
            }
        });
    }

    receiver.dataTable = function(){
        return $('#receiverListTable').DataTable({
            destroy: true,
            ajax: {
                url:'/mail/receiver/group',
                type:'GET'
            },
            order: [[ 0, "asc" ]],
            searching: true,
            ordering:  true,
            paging:  true,
            pagingType: "full_numbers",
            language: {
                search: "_INPUT_",
                searchPlaceholder: "Search records",
            },
            lengthMenu: [
                [10, 25, 50, -1],
                [10, 25, 50, "All"]
            ],
            columns:[
                {"data" : "addrGrpName"},
                {"data" : "addrGrpMbrCnt"},
                {
                    "defaultContent":'그룹대상관리'
                },
                {"data" : "createdDt",
                    render : function(data){
                        return '<td class="text-center">'+moment(data).format('YYYY.MM.DD')+'</td>'
                    }
                },
                {"data" : "modifiedDt",
                    render : function(data){
                        return '<td class="text-center">'+moment(data).format('YYYY.MM.DD')+'</td>'
                    }
                },
                {
                    "defaultContent":'수정'
                },
                // {
                //     "defaultContent":'<i class="delete tim-icons icon-simple-remove"></i>'
                // },
                {
                    "data":"addrGrpId",
                     render : function(data){
                        return '<td class="text-center"><i class="delete tim-icons icon-simple-remove" data-idx="'+data+'"></i></td>'
                     }
                }
            ],
            columnDefs:[
                {className:'text-center', targets:[0,1,2,3,4,5,6]},
                {sortable:false, targets:[6]}
            ],
            initComplete: function () {
                $('.dataTables_filter input[type="search"]').removeClass().addClass('form-control');
            }
        });
    }

    receiver.init();
})(jQuery, {});
