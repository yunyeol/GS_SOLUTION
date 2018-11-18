(function($, usersModal){

    usersModal.init = function(){
        var userTable = $('#userListtable').DataTable();

        usersModal.setFormValidation('#updatePwdValidation');

        this.setEvent(userTable);
    }

    usersModal.setEvent = function(userTable){
        $('select[name="updateSelectAuth"]').change(function(){
            var data = {
                "loginId" : $('#loginId').val(),
                "mbrAuthId" : $('select[name="updateSelectAuth"]').val()
            };

            $.ajax({
                method: "put",
                url: "/settings/users/auth",
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

        $('select[name="updateSelectGrp"]').change(function(){
            var data = {
                "loginId" : $('#loginId').val(),
                "mbrGrpId" : $('select[name="updateSelectGrp"]').val()
            };

            $.ajax({
                method: "put",
                url: "/settings/users/grp",
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
    };

    usersModal.setFormValidation = function(id) {
        $(id).validate({
            highlight: function(element) {
                $(element).closest('.form-group').removeClass('has-success').addClass('has-danger');
                $(element).closest('.form-check').removeClass('has-success').addClass('has-danger');
            },
            success: function(element) {
                $(element).closest('.form-group').removeClass('has-danger').addClass('has-success');
                $(element).closest('.form-check').removeClass('has-danger').addClass('has-success');
            },
            errorPlacement: function(error, element) {
                $(element).closest('.form-group').append(error);
            },
        });
    };

    usersModal.init();
})(jQuery, {});
