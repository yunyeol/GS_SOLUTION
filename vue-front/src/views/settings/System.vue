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
                                            <input id="datatable_filter" type="search" class="form-control form-control-md" placeholder="Search records" aria-controls="datatable" v-model="query.searchParams.keyword" @keyup.enter="search">
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
                                                v-on:dblclick="modifySystemCode(list, index)" v-bind:key="index" >
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
                            <Pagenation url="/system/selectSystemCodeTest" :params="query" @paginated="paginated" ref="systemPagenation"></Pagenation>
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
    import Pagenation from '../../components/Pagination.vue';

    var doubleCheck = false;

    export default {
        name: 'settings',
        components: {
            Left,
            Top,
            SettingsModal,
            Pagenation
        },
        data: function(){
            return {
                query:{
                    searchParams:{
                        keyword : ''
                    }
                },
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

                $(document).on("click.select", "#updateCancel", function () {
                    var index = $(this).attr('data-seq');
                    $("tbody tr:eq("+index+")").show();
                    $("#addTr").remove();

                    doubleCheck = false;
                });

                $(document).on("click.update", "#callUpdate", function () {
                    this.postUpdateSystemCode();
                });
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
            modifySystemCode:async function(list, index){
                if(doubleCheck == true){
                    return;
                }

                doubleCheck = true;

                var changeTr = "<tr id='addTr'>"+
                                    "<td><input type='text' class='form-control text-center' placeholder='ex)0000' minlength='4' maxlength='4' v-model='type' value='"+list.TYPE+"'></td>"+
                                    "<td><input type='text' class='form-control text-center' placeholder='ex)0000' minlength='4' maxlength='4' v-model='gubun' value='"+list.GUBUN+"'></td>"+
                                    "<td><input type='text' class='form-control text-center' maxlength='256' v-model='data1'value='"+list.DATA1+"'></td>"+
                                    "<td><input type='text' class='form-control text-center' maxlength='256' v-model='data2'value='"+list.DATA2+"'></td>"+
                                    "<td><input type='text' class='form-control text-center' maxlength='256' v-model='data3'value='"+list.DATA3+"'></td>"+
                                    "<td>" +
                                        "<button type='button' class='btn btn-primary btn-link' id='callUpdate'>수정</button>" +
                                        "/"+
                                        "<button type='button' class='btn btn-primary btn-link' id='updateCancel' data-seq='"+index+"'>취소</button>" +
                                    "</td>"+
                                "</tr>";

                $("tbody tr:eq("+index+")").hide();
                var obj = $("#datatable > tbody > tr");
                obj.eq(index).after(changeTr);
            },
            postUpdateSystemCode:async function () {
                alert("2");
            },
            paginated(data){
                this.settList = data || [];
            },
            search(){
                this.$refs.systemPagenation.search();
            },
        },
        mounted: function(){
            this.init();

        }
    }
</script>
