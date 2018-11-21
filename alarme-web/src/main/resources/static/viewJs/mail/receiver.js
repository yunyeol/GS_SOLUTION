(function($, receiver){

    receiver.init = function(){
        console.log('타라');
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
                {"data" : "createdDt"},
                {"data" : "modifiedDt"},
                {
                    "defaultContent":'수정'
                },
                {
                    "defaultContent":'<i class="delete tim-icons icon-simple-remove"></i>'
                }
            ],
            columnDefs:[
                {className:'text-center', targets:[0,1,2,3,4,5]},
                {sortable:false, targets:[5]}
            ],
            initComplete: function () {
                $('.dataTables_filter input[type="search"]').removeClass().addClass('form-control');
            }
        });
    }

    receiver.setEvent = function(codeTable){

    }

    receiver.init();
})(jQuery, {});
