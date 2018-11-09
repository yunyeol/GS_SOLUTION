<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left mail-menu-expand="false" mail-menu-show="collapse"
                  settings-menu-active="active" settings-menu-expand="true" settings-menu-show="collapse show" settings-sub-menu-active1="active" settings-sub-menu-active2="">
            </Left>
        </div>
        <div class="main-panel ps ps--active-y">
            <Top title="Settings"></Top>

            <div class="content">
                <div class="card">
                    <div class="card-header">
                        <div class="row">
                            <h4 class="col-sm-11">
                                Settings
                            </h4>
                            <div class="col-sm-1">
                                <button style="float:right;" class="btn btn-primary btn-sm tim-icons icon-simple-add"
                                        data-toggle="modal" data-target="#settingsModal"></button>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div id="datatable_wrapper" class="dataTables_wrapper dt-bootstrap4">
                            <div class="row">
                                <div class="col-sm-12 col-md-6">
                                    <div class="dataTables_length" id="datatable_length">
                                        <label>Show
                                            <select name="datatable_length" aria-controls="datatable" class="custom-select custom-select-sm form-control form-control-sm">
                                                <option value="10">10</option>
                                                <option value="25">25</option>
                                                <option value="50">50</option>
                                                <option value="-1">All</option>
                                            </select> entries</label>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-6">
                                    <div class="dataTables_filter">
                                        <label>
                                            <input id="datatable_filter" type="search" class="form-control form-control-md" placeholder="Search records" aria-controls="datatable">
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    테이블 행(row)을 더블클릭하시면 수정하실수 있습니다.
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <table id="datatable" class="table tablesorter" role="grid" aria-describedby="datatable_info" style="width:100%">
                                        <thead>
                                            <tr role="row">
                                                <th class="sorting_asc text-center header" tabindex="0" aria-controls="datatable" style="width:7%" >타입</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:7%">구분</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:20%">데이터1</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:20%">데이터2</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:20%">데이터3</th>
                                                <th class="text-center" tabindex="0" aria-controls="datatable"  style="width:9%">삭제</th>
                                            </tr>
                                        </thead>
                                        <tbody >
                                            <tr v-if="settList && settList.length > 0" v-for="(list, index) in settList"
                                                v-on:dblclick="postUpdateSystemCode(list, index)" >
                                                <td class="text-center">{{list.TYPE}}</td>
                                                <td class="text-center">{{list.GUBUN}}</td>
                                                <td class="text-center">{{list.DATA1}}</td>
                                                <td class="text-center">{{list.DATA2}}</td>
                                                <td class="text-center">{{list.DATA3}}</td>
                                                <td class="text-center" v-on:click="deleteDeleteSystemCode(list)"><i class="tim-icons icon-simple-remove"></i></td>
                                            </tr>
                                            <tr v-else>
                                                <td class="text-center" colspan="6">데이터가 존재하지 않습니다.</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <SettingsModal></SettingsModal>
    </div>
</template>

<script>
    // @ is an alias to /src
    import Left from "../../components/Left.vue";
    import Top from "../../components/Top.vue";
    import SettingsModal from "../../components/settings/system/Modal.vue";

    export default {
        name: 'settings',
        components: {
            Left,
            Top,
            SettingsModal
        },
        data: function(){
            return {
                settList:[]
            }
        },
        methods:{
            init : function () {
                this.$on('selectSystemCode',this.getSelectSystemCode);

                setTimeout(function(){
                    $("#datatable").tablesorter({
                        headers:{
                            0:{sorter:'NumberSort'},
                            1:{sorter:'NumberSort'},
                            2:{sorter:'TextSort'},
                            3:{sorter:'TextSort'},
                            4:{sorter:'TextSort'},
                            5:{sorter:false}
                        }
                    });
                }, 500);

                $(document).on("keyup", "#datatable_filter", function () {
                    var value = $(this).val().toLowerCase();

                    $("#datatable tbody tr").filter(function() {
                        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                    });
                });

                // $("#datatable_filter").on('keyuo', function(){
                //     var value = $(this).val().toLowerCase();
                //
                //     alert(value);
                //
                //     $("#datatable tr").filter(function() {
                //         $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                //     });
                // });

                //
                // var table = $('#datatable').DataTable({
                //     destroy: true,
                //     ajax: {
                //         url:this.$API_URL+'/system/selectSystemCode',
                //         type:'GET',
                //         dataSrc:""
                //     },
                //     columns:[
                //         {"data" : "TYPE"},
                //         {"data" : "GUBUN"},
                //         {"data" : "DATA1"},
                //         {"data" : "DATA2"},
                //         {"data" : "DATA3"},
                //         {
                //             "defaultContent":'<i class="tim-icons icon-simple-remove" id="deleteSystemCode"></i>'
                //         }
                //     ],
                //     columnDefs:[
                //         {"targets":[0,1,2,3,4,5], "className":"text-center"}
                //     ],
                //     pagingType: "full_numbers",
                //     lengthMenu: [
                //         [10, 25, 50, -1],
                //         [10, 25, 50, "All"]
                //     ],
                //     responsive: true,
                //     language: {
                //         search: "_INPUT_",
                //         searchPlaceholder: "Search records",
                //     }
                // });

                // $(document).on('click', '#deleteSystemCode', function () {
                //     var data = table.row( this ).data();
                //     alert(data);
                //     swal({
                //         title: 'Are you sure?',
                //         text: "You won't be able to revert this!",
                //         type: 'warning',
                //         showCancelButton: true,
                //         confirmButtonClass: 'btn btn-success',
                //         cancelButtonClass: 'btn btn-danger',
                //         confirmButtonText: 'Yes, delete it!',
                //         buttonsStyling: false
                //     }).then(function() {
                //         swal({
                //             title: 'Deleted!',
                //             text: 'Your file has been deleted.',
                //             type: 'success',
                //             confirmButtonClass: "btn btn-success",
                //             buttonsStyling: false
                //         });
                //     }).catch(swal.noop);
                // });
            },
            getSelectSystemCode: async function(){
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/selectSystemCode',
                    method: 'get',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{
                        "whereAdd":"N"
                    }
                }).catch (err => console.error(err))

                if(rv && rv['data']) {
                    this.settList = rv['data'];
                }

            },
            deleteDeleteSystemCode: function(list){
                var self = this;
                swal({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonClass: 'btn btn-success',
                    cancelButtonClass: 'btn btn-danger',
                    confirmButtonText: 'Yes, delete it!',
                    buttonsStyling: false
                }).then(function() {
                    self.callDeleteSystemCodet(list);

                    swal({
                        title: 'Deleted!',
                        text: 'Your file has been deleted.',
                        type: 'success',
                        confirmButtonClass: "btn btn-success",
                        buttonsStyling: false
                    });

                }).catch(swal.noop);
            },
            callDeleteSystemCodet:async function (list) {
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/deleteSystemCode',
                    method: 'delete',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{"type":list.TYPE, "gubun":list.GUBUN}
                }).catch (err => console.error(err));

                this.getSelectSystemCode();
            },
            postUpdateSystemCode:async function(list, index){
                var changeTr = "<tr>"+
                                    "<td><input type='text' class='form-control' placeholder='ex)0000' minlength='4' maxlength='4' v-model='type'></td>"+
                                    "<td><input type='text' class='form-control' placeholder='ex)0000' minlength='4' maxlength='4' v-model='gubun'></td>"+
                                    "<td><input type='text' class='form-control' maxlength='256' v-model='data1'></td>"+
                                    "<td><input type='text' class='form-control' maxlength='256' v-model='data2'></td>"+
                                    "<td><input type='text' class='form-control' maxlength='256' v-model='data3'></td>"+
                                    "<td>" +
                                        "<button type='button' class='btn btn-primary btn-link' v-on:click='selectDuplicateSystemCode'>수정</button>" +
                                        "/"+
                                        "<button type='button' class='btn btn-primary btn-link' onclick='test()'>취소</button>" +
                                    "</td>"+
                                "</tr>";
                $("tbody tr:eq("+index+")").replaceWith(changeTr);
            }
        },
        mounted: function(){
            this.init();
            this.getSelectSystemCode();
        }
    }
</script>
