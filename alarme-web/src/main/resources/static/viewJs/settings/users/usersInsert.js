(function($, usersInsert){

    var dupCheck = false;

    usersInsert.init = function(){
        usersInsert.setFormValidation('#insertUsersValidation');

        this.setEvent();
    };

    usersInsert.setEvent = function(){
        $('#dupUsersBtn').on('click', function () {
            if($('input[name="loginId"]').val() == ''){
                alert("로그인 아이디를 입력해주세요.");
                return ;
            }

            var data = {
                "loginId" : $('input[name="loginId"]').val()
            };

            $.ajax({
                method: "get",
                url: "/settings/users/condition",
                data: data,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data) {
                    if(data.code == "dup"){
                        alert("사용중인 로그인 아이디 입니다.");
                    }else{
                        alert("사용할수 있는 로그인 아이디 입니다.");
                        dupCheck = true;
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                }
            });
        });

        $('#init').on('click.init', function(){
            dupCheck = false;

            $('input[name="loginId"]').val('');
            $('input[name="passwd"]').val('');
            $('input[name="passwordConfirm"]').val('');
            $('input[name="name"]').val('');
        });

        $('#insertUsersValidation').on('submit', function(){
            if(dupCheck == false){
                alert("중복체크를 해주세요");
                return false;
            }
        });
    };

    usersInsert.setFormValidation = function(id) {
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

    usersInsert.init();
})(jQuery, {});
