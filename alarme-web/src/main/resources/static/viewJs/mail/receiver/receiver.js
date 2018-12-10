(function($, receiver){

    receiver.field = {
        grpNameChk : false
    }
    receiver.init = function(){
        this.dataTable();
        this.setEvent();
    }

    receiver.setEvent = function(){
        var form = $('#receiverForm');

        $('#dupUsersBtn').off('click.receiver').on('click.receiver',function(){

            var params = {};
            params.addrGrpName = $('#receiverForm input.form-control').val();

            var sCallBack = function(data) {
                receiver.field.grpNameChk = ( data ) ? true : false;
                console.log(receiver.field.grpNameChk);
            }

            receiver.ajaxCall('get','/mail/receiver/group/checkname',params,sCallBack);
        });

        $('button[type="submit"]').on('click.receiver',function(){
            form.validate();
            console.log(form.valid());

            var params = {};
            params.addrGrpName = $('#receiverForm input.form-control').val();
            params.loginId = $('input[type=hidden]#loginId').val();

            if( form.valid() ){
                console.log('true');
            }else{
                console.log('why?');
                alert('그룹이름을 입력해 주시기 바랍니다.');
            }
        });
    }

    receiver.dataTable = function(){
        $('#receiverListTable').DataTable({
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
                {
                    "defaultContent":'<i class="delete tim-icons icon-simple-remove"></i>'
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

    receiver.ajaxCall = function(httpType, url, params, sCallBack){
        $.ajax({
            type: httpType,
            dataType : 'json',
            data : params,
            url : url,
            success:function(data){
                if( jQuery.isFunction(sCallBack) ){
                    sCallBack(data);
                }
            },
            error:function(){
            }
        });
    }

    receiver.init();
})(jQuery, {});
