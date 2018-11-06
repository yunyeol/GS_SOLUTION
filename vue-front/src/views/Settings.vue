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
                                        <tr v-for="list in testData">
                                            <td class="sorting_1 text-center">{{list.type}}</td>
                                            <td class="text-center">{{list.gubun}}</td>
                                            <td class="text-center">{{list.data1}}</td>
                                            <td class="text-center">{{list.data2}}</td>
                                            <td class="text-center">{{list.data3}}</td>
                                            <td class="text-center">
                                                <i class="tim-icons icon-simple-remove"></i>
                                            </td>
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
                settList : [],
                testData : [
                    {type:"0000",gubun:"0001",data1:"test1",data2:"",data3:"test"},
                    {type:"0001",gubun:"0001",data1:"test2",data2:"",data3:"test"},
                    {type:"0002",gubun:"0001",data1:"test3",data2:"",data3:"test"},
                    {type:"0003",gubun:"0001",data1:"test4",data2:"",data3:"test"},
                    {type:"0004",gubun:"0001",data1:"test5",data2:"",data3:"test"},
                    {type:"0005",gubun:"0001",data1:"test6",data2:"",data3:"test"},
                    {type:"0006",gubun:"0001",data1:"test7",data2:"",data3:"test"},
                    {type:"0007",gubun:"0001",data1:"test8",data2:"",data3:"test"},
                    {type:"0008",gubun:"0001",data1:"test9",data2:"",data3:"test"},
                    {type:"0009",gubun:"0001",data1:"test10",data2:"",data3:"test"}
                ]
            }
        },
        methods:{
            init : function () {
                $("#datatable").tablesorter();

                $('#datatable').DataTable({
                    "pagingType": "full_numbers",
                    //"scrollY": 300,
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
            },
            // getAxios: async function(){
            //     const rv = await this.$axios({
            //         url: this.$API_URL+'/user/settings',
            //         method: 'get',
            //         timeout: 3000,
            //         headers: {
            //             'Content-Type': 'application/json'
            //         }
            //     }).catch (err => console.error(err))
            //
            //     console.log(rv);
            //
            //     if(rv && rv['data']) {
            //         console.log(rv['data']);
            //     }
            //     this.settList = rv['data']
            // }
        },
        mounted: function(){
            this.init();
            //this.getAxios();
        }
    }
</script>
