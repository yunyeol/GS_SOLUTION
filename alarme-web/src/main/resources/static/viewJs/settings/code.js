(function($, code){

    code.init = function(){
        $('#codeListtable').DataTable({
            "pagingType": "full_numbers",
            "lengthMenu": [
                [10, 25, 50, -1],
                [10, 25, 50, "All"]
            ],
            responsive: true,
            language: {
                search: "_INPUT_",
                searchPlaceholder: "Search records",
            }
        });
        console.log('test');
        //this.setEvent();
    }

    code.setEvent = function(){

    }

    code.ttt = function(){

    }

    code.init();
})(jQuery, {})
