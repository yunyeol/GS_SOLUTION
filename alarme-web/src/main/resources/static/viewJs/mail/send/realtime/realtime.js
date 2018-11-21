(function($, realtime){

    realtime.init = function(){

        CKEDITOR.replace( 'editor', {
            toolbar: [
                { name: 'document', items: [ 'Source' ] },
                { name: 'styles', items: [ 'Format', 'Font', 'FontSize' ] },
                { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike'] },
                { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
                { name: 'align', items: [ 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ] },
                { name: 'links', items: [ 'Link', 'Unlink' ] },
                { name: 'insert', items: [ 'Image', 'Table' ] },
                { name: 'Mapping', items:['LoginId', 'UserName', 'Title', 'Contents']}
            ],
            customConfig: '',
            height: 400,
        });


        this.setEvent();
    };

    realtime.setEvent = function(){
        // $('button[name="realtimeSetting"]').on('click.setting', function(){
        //     alert("test");
        // });
    };

    realtime.init();
})(jQuery, {});
