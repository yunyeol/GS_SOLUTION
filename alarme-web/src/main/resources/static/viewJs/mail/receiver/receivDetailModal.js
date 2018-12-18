var receiverObj = (function($, receivDetailModal){
    receivDetailModal.field = {
        addrGrpId : null
    }

    receivDetailModal.init = function(){
        this.setEvent();
    }

    receivDetailModal.setEvent = function(){
        if(receiverObj && receiverObj.dataTable){
            receiverObj.field.dataTable.off('click.receiverDetailModal').on('click.receiverDetailModal','button[name="receiverDetail"]',function(e){
                receivDetailModal.field.addrGrpId = $(e.currentTarget).data('idx');
                receivDetailModal.dataTable();
            });
        }
    }

    receivDetailModal.dataTable = function(){
        console.log('dataTable :'+receivDetailModal.field.addrGrpId);

        console.log(typeof receivDetailModal.field.addrGrpId);
        console.log(new Date());

        var params = {};
        params.addrGrpId = receivDetailModal.field.addrGrpId;

        return $('#receiverDetailListTable').DataTable({
            destroy: true,
            ajax: {
                url:'/mail/receiver/group/detail',
                type:'GET',
                data:receivDetailModal.field || ''
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
                {"data" : "address"},
                {"data" : "name"},
                {"data" : "createdDt",
                    render : function(data){
                        return '<td class="text-center">'+moment(data).format('YYYY.MM.DD')+'</td>'
                    }
                },
                {"data" : "modifiedDt",
                    render : function(data) {
                        return '<td class="text-center">' + moment(data).format('YYYY.MM.DD') + '</td>'
                    }
                },
                {
                    "data":"addrMbrId",
                    render : function(data){
                        return '<td class="text-center"><i class="delete tim-icons icon-simple-remove" data-idx="'+data+'"></i></td>'
                    }
                }
            ],
            columnDefs:[
                {className:'text-center', targets:[0,1,2,3,4,5]},
                {sortable:false, targets:[5]}
            ],
            initComplete: function () {
                console.log(new Date());
                $('.dataTables_filter input[type="search"]').removeClass().addClass('form-control');
            }
        });
    }

    receivDetailModal.init();
})(jQuery, {});
