(function($, codeModal){

    var dupCheck = false;

    codeModal.init = function(){
        codeModal.setFormValidation('#insertCodeValidation');

        codeModal.setEvent();
    };

    codeModal.setEvent = function(){
        $('#dupBtn').on('click.dup', function(){

            if($('input[name="type"]').val() == '' ||
                $('input[name="gubun"]').val() == ''){
                alert("타입, 구분은 필수 입력값입니다.");
                return ;
            }

            var data = {
                "type" : $('input[name="type"]').val(),
                "gubun" : $('input[name="gubun"]').val()
            };

            $.ajax({
                method: "get",
                url: "/settings/code/condition",
                data: data,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data) {
                    if(data.code == "dup"){
                        alert("사용중인 코드입니다.");
                    }else{
                        alert("사용할수 있는 코드입니다.");
                        dupCheck = true;
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                }
            });
        });

        $('#init').on('click', function(){
            dupCheck = false;
            $('input[name="type"]').val('');
            $('input[name="gubun"]').val('');
            $('input[name="data1"]').val('');
            $('input[name="data2"]').val('');
            $('input[name="data3"]').val('');

            $(':submit').html('입력');
        });

        $('#insertCodeValidation').on('submit', function(){
            if(dupCheck == false){
                alert("중복체크를 해주세요");
                return false;
            }
        });
    };

    codeModal.setFormValidation = function(id) {
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

    codeModal.init();
})(jQuery, {});
