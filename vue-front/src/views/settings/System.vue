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
                                            <select v-model="query.params.option" @change="search" name="datatable_length" aria-controls="datatable"  class="selectpicker " data-style="select-with-transition" title="10" >
                                                <option :value="10">10</option>
                                                <option :value="25">25</option>
                                                <option :value="50">50</option>
                                            </select> entries
                                        </label>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-6">
                                    <div class="dataTables_filter">
                                        <label>
                                            <input id="datatable_filter" type="search" class="form-control form-control-md" placeholder="Search records" aria-controls="datatable" v-model="query.params.keyword" @keyup.enter="search">
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
                                                <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:7%" >타입</th>
                                                <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:7%">구분</th>
                                                <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:20%">데이터1</th>
                                                <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:20%">데이터2</th>
                                                <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:20%">데이터3</th>
                                                <th class="text-center" tabindex="0" aria-controls="datatable"  style="width:10%">삭제</th>
                                            </tr>
                                        </thead>
                                        <tbody v-if="settList && settList.length > 0">
                                            <tr v-for="(list, index) in settList"
                                                v-on:dblclick="modifySystemCode(list, index)" v-bind:key="index" >
                                                <td class="text-center">{{list.TYPE}}</td>
                                                <td class="text-center">{{list.GUBUN}}</td>
                                                <td class="text-center">{{list.DATA1}}</td>
                                                <td class="text-center">{{list.DATA2}}</td>
                                                <td class="text-center">{{list.DATA3}}</td>
                                                <td class="text-center" v-on:click="deleteDeleteSystemCode(list)"><i class="tim-icons icon-simple-remove"></i></td>
                                            </tr>
                                        </tbody>
                                        <tbody v-else>
                                            <tr >
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
                    params:{
                        keyword : '',
                        option:''
                    }
                },
                settList:[]
            }
        },
        methods:{
            init : function () {
                $(document).on("click.select", "#updateCancel", function () {
                    var index = $(this).attr('data-seq');
                    $("tbody tr:eq("+index+")").show();
                    $("#addTr").remove();

                    doubleCheck = false;
                });

                $(document).on("click.update", "#callUpdate", async function () {
                    var index = $(this).attr('data-seq');

                    var data = {
                        "type" : $('#type').val(),
                        "gubun" : $('#gubun').val(),
                        "data1" : $('#data1').val(),
                        "data2" : $('#data2').val(),
                        "data3" : $('#data3').val()
                    };

                    $.ajax({
                        method: "put",
                        url: "http://127.0.0.1:10009/api/system/code",
                        data: JSON.stringify(data),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function(data) {
                            // $("#addTr").remove();
                            // $("tbody tr:eq("+index+")").show();
                            location.reload();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {

                        }
                    });
                });
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
                    url: this.$API_URL+'/system/code',
                    method: 'delete',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{"type":list.TYPE, "gubun":list.GUBUN}
                }).catch (err => console.error(err));

                this.$refs.systemPagenation.search();
            },
            modifySystemCode:async function(list, index){
                if(doubleCheck == true){
                    return;
                }

                doubleCheck = true;

                var changeTr = "<tr id='addTr'>"+
                                    "<td class='text-center'><input type='text' class='form-control text-center' placeholder='ex)0000' minlength='4' maxlength='4' id='type' value='"+list.TYPE+"'></td>"+
                                    "<td class='text-center'><input type='text' class='form-control text-center' placeholder='ex)0000' minlength='4' maxlength='4' id='gubun' value='"+list.GUBUN+"'></td>"+
                                    "<td class='text-center'><input type='text' class='form-control text-center' maxlength='256' id='data1'value='"+list.DATA1+"'></td>"+
                                    "<td class='text-center'><input type='text' class='form-control text-center' maxlength='256' id='data2'value='"+list.DATA2+"'></td>"+
                                    "<td class='text-center'><input type='text' class='form-control text-center' maxlength='256' id='data3'value='"+list.DATA3+"'></td>"+
                                    "<td class='text-center'>" +
                                        "<button type='button' class='btn btn-primary btn-link' id='callUpdate' data-seq='"+index+"'>수정</button>" +
                                        "/"+
                                        "<button type='button' class='btn btn-primary btn-link' id='updateCancel' data-seq='"+index+"'>취소</button>" +
                                    "</td>"+
                                "</tr>";

                $("tbody tr:eq("+index+")").hide();
                var obj = $("#datatable > tbody > tr");
                obj.eq(index).after(changeTr);
            },
            paginated(data){
                this.settList = data || [];
            },
            search(){
                this.$refs.systemPagenation.search();
            }
        },
        mounted: function(){
            this.init();
        }
    }
</script>
