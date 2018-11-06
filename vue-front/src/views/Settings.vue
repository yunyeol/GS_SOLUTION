<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left mail-menu-expand="false" mail-menu-show="collapse" settings-menu-active="active">
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
                                    <table id="datatable" class="table" role="grid" aria-describedby="datatable_info" style="width:100%">
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
                                        <tbody>
                                            <!--<tr v-for="(item,index) in settList" v-bind:key="index" v-cloak>-->
                                                <!--<td class="sorting_1 text-center">{{item.TYPE}}</td>-->
                                                <!--<td class="text-center">{{item.GUBUN}}</td>-->
                                                <!--<td class="text-center">{{item.DATA1}}</td>-->
                                                <!--<td class="text-center">{{item.DATA2}}</td>-->
                                                <!--<td class="text-center">{{item.DATA3}}</td>-->
                                                <!--<td class="text-center">-->
                                                    <!--<i class="tim-icons icon-simple-remove"></i>-->
                                                <!--</td>-->
                                            <!--</tr>-->
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
    import Left from "../components/Left.vue";
    import Top from "../components/Top.vue";
    import SettingsModal from "../components/settings/Modal.vue";

    export default {
        name: 'settings',
        components: {
            Left,
            Top,
            SettingsModal
        },
        data: function(){
            return {

            }
        },
        methods:{
            init : function () {
                $('#datatable').DataTable({
                    destroy: true,
                    ajax: {
                        url:this.$API_URL+'/system/profile',
                        type:'GET',
                        dataSrc:""
                    },
                    columns:[
                        {"data" : "TYPE"},
                        {"data" : "GUBUN"},
                        {"data" : "DATA1"},
                        {"data" : "DATA2"},
                        {"data" : "DATA3"},
                        {
                            "defaultContent":'<i class="tim-icons icon-simple-remove"></i>'
                        }
                    ],
                    columnDefs:[
                        {"className":"text-center", "targets":[0,1,2,3,4,5]}
                    ],
                    order:[[0, 'asc']],
                    pagingType: "full_numbers",
                    lengthMenu: [
                        [10, 25, 50, -1],
                        [10, 25, 50, "All"]
                    ],
                    responsive: true,
                    language: {
                        search: "_INPUT_",
                        searchPlaceholder: "Search records",
                    }
                });

                $("#datatable").tablesorter();
            }
        },
        mounted: function(){
            this.init();
        }
    }
</script>
