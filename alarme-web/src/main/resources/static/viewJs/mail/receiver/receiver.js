(function($, receiver){

    receiver.init = function(){
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
        $('#insertReceiverValidation').validate();
    }

    receiver.setEvent = function(){
        $('#insertReceiverValidation').validate();
    }

    receiver.init();
})(jQuery, {});
