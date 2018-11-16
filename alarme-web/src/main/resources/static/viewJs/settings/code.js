(function($, code){

    code.init = function(){
        $('#codeListtable').DataTable({
            destroy: true,
            ajax: {
                url:'http://localhost:8080/settings/code/ajax',
                type:'GET',
                dataSrc : function (json) {
                    console.log(json);
                    return json.data;
                }
            },
            "pagingType": "full_numbers",
            "lengthMenu": [
                [10, 25, 50, -1],
                [10, 25, 50, "All"]
            ],
            columns:[
                {"data" : "TYPE"},
                {"data" : "GUBUN"},
                {"data" : "DATA1"},
                {"data" : "DATA2"},
                {"data" : "DATA3"},
                {
                    "defaultContent":'<i class="tim-icons icon-simple-remove"></i>'
                }
            ]
        });

        // $('#codeListtable').DataTable({
        //     "pagingType": "full_numbers",
        //     "lengthMenu": [
        //         [10, 25, 50, -1],
        //         [10, 25, 50, "All"]
        //     ],
        //     responsive: true,
        //     language: {
        //         search: "_INPUT_",
        //         searchPlaceholder: "Search records",
        //     }
        // });

        var codeTable = $('#codeListtable').DataTable();

        this.setEvent(codeTable);
    }

    code.setEvent = function(codeTable){
        codeTable.on('click.delete', '.delete', function(){
            $tr = $(this).closest('tr');
            var columnData = codeTable.row($tr).data();

            var data = {
                "type" : columnData[0],
                "gubun" : columnData[1]
            };

            $.ajax({
                method: "delete",
                url: "/settings/code",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                //dataType: "json",
                success: function(data) {
                    console.log(data);
                    codeTable.ajax.reload();
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                }
            });
        });
    }

    code.init();
})(jQuery, {});
