(function($, realtime){

    realtime.init = function(){

        CKEDITOR.replace( 'editor');


        this.setEvent();
    };

    realtime.setEvent = function(){
        // $('button[name="realtimeSetting"]').on('click.setting', function(){
        //     alert("test");
        // });
    };

    realtime.init();
})(jQuery, {});
