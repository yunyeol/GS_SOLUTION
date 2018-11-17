(function($, code){

    code.init = function(){
        $('#codeListtable').DataTable({
            destroy: true,
            ajax: {
                url:'/settings/code/table',
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
                {"data" : "type"},
                {"data" : "gubun"},
                {"data" : "data1"},
                {"data" : "data2"},
                {"data" : "data3"},
                {
                    "defaultContent":'<i class="delete tim-icons icon-simple-remove"></i>'
                }
            ],
            columnDefs:[
                { className: 'text-center', targets: [0, 1, 2, 3, 4, 5] }
            ],
            initComplete: function () {
                $('.dataTables_filter input[type="search"]').removeClass().addClass('form-control');
                // $('select[name="codeListtable_length"]').removeClass().addClass('selectpicker');
                // $('select[name="codeListtable_length"]').attr('data-style', 'select-with-transition');
            }
        });

        var codeTable = $('#codeListtable').DataTable();

        this.setEvent(codeTable);
    }

    code.setEvent = function(codeTable){
        codeTable.on('click.delete', '.delete', function (){
            $tr = $(this).closest('tr');
            var columnData = codeTable.row($tr).data();

            var data = {
                "type" : columnData.type,
                "gubun" : columnData.gubun
            };

            if(confirm("정말 삭제하시겠습니까??") == true){
                $.ajax({
                    method: "delete",
                    url: "/settings/code",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function(data) {
                        if(data.code == "success"){
                            codeTable.ajax.reload();
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                    }
                });
            }else{
                return;
            }
        });
    }

    code.init();
})(jQuery, {});
