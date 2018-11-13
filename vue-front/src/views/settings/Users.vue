<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left mail-menu-expand="false" mail-menu-show="collapse"
                  settings-menu-active="active" settings-menu-expand="true" settings-menu-show="collapse show" settings-sub-menu-active1="" settings-sub-menu-active2="active">
            </Left>
        </div>
        <div class="main-panel ps ps--active-y">
            <Top title="Users"></Top>

            <div class="content">
                <div class="card">
                    <div class="card-header">
                        <div class="card-header">
                            <div class="row">
                                <h4 class="col-sm-11">
                                    Users
                                </h4>
                                <div class="col-sm-1">
                                    <button style="float:right;" class="btn btn-primary btn-sm tim-icons icon-simple-add"
                                            data-toggle="modal" data-target="#settingsModal"></button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="card-body">
                            <div id="datatable_wrapper" class="dataTables_wrapper dt-bootstrap4">
                                <div class="row">
                                    <div class="col-sm-12 col-md-6">
                                        <div class="dataTables_length" id="datatable_length">
                                            <label>Show
                                                <select v-model="option" @change="pageLength" name="datatable_length" aria-controls="datatable" class="custom-select custom-select-sm form-control form-control-sm" >
                                                    <option :value="10">10</option>
                                                    <option :value="25">25</option>
                                                    <option :value="50">50</option>
                                                    <option :value="-1">All</option>
                                                </select> entries</label>
                                        </div>
                                    </div>
                                    <div class="col-sm-12 col-md-6">
                                        <div class="dataTables_filter">
                                            <label>
                                                <input id="datatable_filter" v-model="keyWord" @keyup.enter="getUsers(10, 0)" type="search" class="form-control form-control-md" placeholder="Search records" aria-controls="datatable">
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <table id="datatable" class="table tablesorter" role="grid" aria-describedby="datatable_info" style="width:100%">
                                            <thead>
                                                <tr role="row">
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:20%" >로그인ID</th>
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:15%" >이름</th>
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:10%">비밀번호</th>
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:10%">그룹</th>
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:10%">권한</th>
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable" style="width:9%">활성화</th>
                                                    <th class=" text-center " tabindex="0" aria-controls="datatable"  style="width:7%">삭제</th>
                                                </tr>
                                            </thead>
                                            <tbody >
                                                <tr v-if="userList && userList.length > 0" v-for="(list, index) in userList" v-bind:key="index"  >
                                                    <td class="text-center">{{list.LOGIN_ID}}</td>
                                                    <td class="text-center">{{list.MBR_NAME}}</td>
                                                    <td class="text-center">
                                                        <a href="#" class="badge badge-primary">변경</a>
                                                    </td>
                                                    <td class="text-center">{{list.GRP_NAME}}</td>
                                                    <td class="text-center">{{list.AUTH_NAME}}</td>
                                                    <td class="text-center">
                                                        <div class="bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-off bootstrap-switch-animate" style="width: 68px;">
                                                            <div class="bootstrap-switch-container" style="width: 118px; margin-left: -50px;">
                                                                <span class="bootstrap-switch-handle-on bootstrap-switch-primary" style="width: 50px;">ON</span>
                                                                <span class="bootstrap-switch-label" style="width: 30px;">&nbsp;</span>
                                                                <span class="bootstrap-switch-handle-off bootstrap-switch-default" style="width: 50px;">OFF</span>
                                                                <input type="checkbox" name="checkbox" class="bootstrap-switch" data-on-label="ON" data-off-label="OFF" v-on:click="toggleBtn">
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td class="text-center" v-on:click="deleteUser(list)">
                                                        <i class="tim-icons icon-simple-remove"></i>
                                                    </td>
                                                </tr>
                                                <tr v-else>
                                                    <td class="text-center" colspan="7">데이터가 존재하지 않습니다.</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <!--
                                페이징 처리 : 페이징 뷰 선언  @getUsers="getUsers" 이부분은 호출한 메소드명으로 대체
                                            url은 page 추가하여 전체 건수 가져오는 쿼리를 수행
                                -->
                                <Paging url="/system/users/page"
                                        :list-method-name="listMethodName"
                                        :key-word="keyWord"
                                        :option="option"
                                        @getUsers="getUsers"
                                        @getStartPage="getStartPage"
                                        ref="paging">
                                </Paging>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import Left from "../../components/Left.vue";
    import Top from "../../components/Top.vue";
    import Paging from "../../components/Paging.vue";

    //페이징 처리 : 이페이지에 초기 들어왔을때 페이지 세팅값
    var CURRENT_PAGE = 0;

    export default {
        name: "User",
        components: {
            Left,
            Top,
            Paging //페이징 처리 : 페이징 뷰 추가
        },
        data : function () {
            return {
                userList : [],
                keyWord:'',
                option : 10,

                //페이징 처리 : 페이징 뷰에 전달할 데이터 선언
                rowGroup : 10,
                listMethodName : 'getUsers',
                startPage:0
            }
        },
        methods:{
            init:function () {
            },
            pageLength : function(){
                this.getUsers(this.option, this.startPage);
            },
            //파라미터로 시작값과 로우건수를 입력, 초기값 0, 10
            getUsers: async function (rowGroup, startPage) {
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/users',
                    method: 'get',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{
                        "startPage" : startPage,
                        "rowGroup" : rowGroup,
                        "keyWord" : this.keyWord
                    }
                }).catch (err => console.error(err));

                if(rv && rv['data']) {
                    this.userList = rv['data'];
                }

                this.$refs.paging.getTotalpageData();
            },
            toggleBtn:function () {
                alert("t");

                console.log($(this));
                var data_on_label = $(this).data('on-label') || '';
                var data_off_label = $(this).data('off-label') || '';

                $(this).bootstrapSwitch({
                    onText: data_on_label,
                    offText: data_off_label
                });
            },
            deleteUser: function (list) {
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
                    self.callDeleteUser(list);

                    swal({
                        title: 'Deleted!',
                        text: 'Your file has been deleted.',
                        type: 'success',
                        confirmButtonClass: "btn btn-success",
                        buttonsStyling: false
                    });

                }).catch(swal.noop);
            },
            callDeleteUser : async function(list){
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/users',
                    method: 'delete',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{"loginId" : list.LOGIN_ID}
                }).catch (err => console.error(err));

                //페이징 처리 : 어떠한 처리(insert, delete, update)후 다시 데이터를 가져올때 사용하는 메소드
                this.reflesh();
            },
            getStartPage:function(startPage){
                this.startPage = startPage || [];
            },
            //페이징 처리 : 어떠한 처리(insert, delete, update)후 다시 데이터를 가져올때 사용하는 메소드
            reflesh() {
                this.getUsers(this.option, this.startPage);
                this.$refs.paging.getTotalpageData();
            }
        },
        mounted:function () {
            this.init();
            //페이징 처리 : 이페이지 처음 진입시 페이징 처리
            this.getUsers(this.option, CURRENT_PAGE);
        }
    }
</script>

<style scoped>

</style>
