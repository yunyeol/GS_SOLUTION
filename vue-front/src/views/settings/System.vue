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
                                <div class="col-sm-12">
                                    <table id="datatable" class="table tablesorter" role="grid" aria-describedby="datatable_info" style="width:100%">
                                        <thead>
                                            <tr role="row">
                                                <th class="sorting_asc text-center header" tabindex="0" aria-controls="datatable" style="width:7%" >타입</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:7%">구분</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:20%">데이터1</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:20%">데이터2</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width:20%">데이터3</th>
                                                <th class="text-center" tabindex="0" aria-controls="datatable"  style="width:7%">삭제</th>
                                            </tr>
                                        </thead>
                                        <tbody >
                                            <tr v-if="settList && settList.length > 0" v-for="list in settList">
                                                <td class="text-center">{{list.TYPE}}</td>
                                                <td class="text-center">{{list.GUBUN}}</td>
                                                <td class="text-center">{{list.DATA1}}</td>
                                                <td class="text-center">{{list.DATA2}}</td>
                                                <td class="text-center">{{list.DATA3}}</td>
                                                <td class="text-center" v-on:click="getDeleteSystemCode(list)"><i class="tim-icons icon-simple-remove"></i></td>
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
                    }
                }).catch (err => console.error(err))

                if(rv && rv['data']) {
                    this.settList = rv['data'];
                }

            },
            getDeleteSystemCode: function(list){
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
            }
        },
        mounted: function(){
            this.init();
            this.getSelectSystemCode();
        }
    }
</script>
