(function($, users){

    users.init = function(){
        $('#userListtable').DataTable({
            destroy: true,
            ajax: {
                url:'/settings/users/table',
                type:'GET'
            },
            order: [[ 0, "asc" ]],
            searching: true,
            ordering:  true,
            paging:  true,
            pagingType: "full_numbers",
            language: {
                dtLang:"ko",
                search: "_INPUT_",
                searchPlaceholder: "Search records",
            },
            lengthMenu: [
                [10, 25, 50, -1],
                [10, 25, 50, "All"]
            ],
            columns:[
                {"data" : "loginId"},
                {"data" : "mbrName"},
                {
                    data : null,
                    render : function(data, type, row){
                        return '<button name="pwdChange" data-toggle="modal" data-id="'+row.loginId+'" ' +
                            'data-target="#pwdModal" class="btn btn-primary btn-simple btn-sm" >변경</button>';
                    }
                },
                {
                    data : 'mbrGrpName',
                    render : function(data, type, row){
                        return '<button name="grpChange" data-toggle="modal" data-id="'+row.loginId+'" ' +
                            'data-target="#grpModal" class="btn btn-primary btn-simple btn-sm" >'+data+'</button>'
                    }
                },
                {
                    "data" : "mbrAuthName",
                    render : function(data, type, row){
                        return '<button name="authChange" data-toggle="modal" data-id="'+row.loginId+'" ' +
                            'data-target="#authModal" class="btn btn-primary btn-simple btn-sm" >'+data+'</button>'
                    }
                },
                {
                    "data" : "activeYn",
                    render : function(data){
                        return '<button name="activeChange" class="btn btn-primary btn-simple btn-sm" >'+data+'</button>'
                    }
                },
                {"defaultContent":'<i class="delete tim-icons icon-simple-remove"></i>'}
            ],
            columnDefs:[
                {className:'text-center', targets:[0,1,2,3,4,5,6]},
                {sortable:false, targets:[2,6]}
            ],
            initComplete: function () {
                $('.dataTables_filter input[type="search"]').removeClass().addClass('form-control');
            }
        });

        var userTable = $('#userListtable').DataTable();

        this.setEvent(userTable);
    }

    users.setEvent = function(userTable){
        userTable.on('click.delete', '.delete', function (){
            $tr = $(this).closest('tr');
            var columnData = userTable.row($tr).data();

            var data = {
                "loginId" : columnData.loginId,
            };

            if(confirm("정말 삭제하시겠습니까??") == true){
                $.ajax({
                    method: "delete",
                    url: "/settings/users",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function(data) {
                        if(data.code == "success"){
                            userTable.ajax.reload();
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

        userTable.on('click.active', 'button[name="activeChange"]', function(){
            $tr = $(this).closest('tr');
            var columnData = userTable.row($tr).data();

            var activeYn;
            if(columnData.activeYn == "Y") {
                activeYn = "N";
            }else{
                activeYn = "Y";
            }

            var data = {
                "loginId" : columnData.loginId,
                "activeYn" : activeYn
            };

            $.ajax({
                method: "put",
                url: "/settings/users",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data) {
                    if(data.code == "success"){
                        userTable.ajax.reload();
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                }
            });

        });

        userTable.on('click.auth', 'button[name="authChange"]', function(){
            $tr = $(this).closest('tr');
            var columnData = userTable.row($tr).data();

            $('#modalSelectAuth').find('.filter-option').html(columnData.mbrAuthName);
            $('#loginId').val($(this).data('id'));
        });

        userTable.on('click.grp', 'button[name="grpChange"]', function(){
            $tr = $(this).closest('tr');
            var columnData = userTable.row($tr).data();

            $('#modalSelectGrp').find('.filter-option').html(columnData.mbrGrpName);
            $('#loginId').val($(this).data('id'));
        });

        userTable.on('click.pwd', 'button[name="pwdChange"]', function(){
            $('#loginId').val($(this).data('id'));
        });
    };

    users.init();
})(jQuery, {});
