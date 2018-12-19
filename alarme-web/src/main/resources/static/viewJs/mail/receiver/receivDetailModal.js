var receiverObj = (function($, receivDetailModal){
    receivDetailModal.field = {
        addrGrpId : null,
        currIdx : 0
    }

    receivDetailModal.init = function(){
        this.setEvent();
    }

    receivDetailModal.setEvent = function(){
        if(receiverObj && receiverObj.dataTable){
            receiverObj.field.dataTable.off('click.receiverDetailModal').on('click.receiverDetailModal','button[name="receiverDetail"]',function(e){
                receivDetailModal.field.addrGrpId = $(e.currentTarget).data('idx');
                receivDetailModal.field.currIdx = 1;

                var sCallBack = function(resultData) {

                    if( resultData && resultData.data ){
                        // 여기부터 진행...
                    }
                    receiver.field.grpNameChk = ( resultData && resultData.data === 'N' ) ? true : false;
                    if( receiver.field.grpNameChk ){
                        alert('그룹이름 : '+grpName+'은 사용 가능합니다.');
                        $('#receiverForm input.form-control').attr( 'disabled', true );
                    }else{
                        alert('그룹이름 : '+grpName+'은 존재합니다. 사용 불가');
                    }
                }

                alarmeCommon.ajaxCall('get','/mail/receiver/group/checkname',params,null,null,sCallBack,null);

                receivDetailModal.setPaging();
            });
        }
    }

    receivDetailModal.setPaging = function(){
        $('#pagination').twbsPagination({
            totalPages: 10,  // 전체 page블럭 수
            visiblePages: 5,  // 출력될 page 블럭수 상수로 지정해 줘도 되고, 변수로 지정해줘도 된다.
            prev: "PREVIOUS",
            next: "NEXT",
            first: 'FIRST',
            last: 'LAST',
            onPageClick: function (event, page) {
                console.log(event);
                console.log(page);
                // $('#page-content').text('Page ' + page);
                // paging(page);
            }
        });
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
