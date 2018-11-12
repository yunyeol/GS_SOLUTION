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
                                                <select  name="datatable_length" aria-controls="datatable" class="custom-select custom-select-sm form-control form-control-sm">
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
                                        <table id="usertable" class="table tablesorter" role="grid" aria-describedby="usertable_info" style="width:100%">
                                            <thead>
                                                <tr role="row">
                                                    <th class="sorting_asc text-center header" tabindex="0" aria-controls="usertable" style="width:20%" >로그인ID</th>
                                                    <th class="sorting text-center header" tabindex="0" aria-controls="usertable" style="width:15%" >이름</th>
                                                    <th class="text-center" tabindex="0" aria-controls="usertable" style="width:10%">비밀번호</th>
                                                    <th class="sorting text-center header" tabindex="0" aria-controls="usertable" style="width:10%">그룹</th>
                                                    <th class="sorting text-center header" tabindex="0" aria-controls="usertable" style="width:10%">권한</th>
                                                    <th class="sorting text-center header" tabindex="0" aria-controls="usertable" style="width:9%">활성화</th>
                                                    <th class="text-center" tabindex="0" aria-controls="usertable"  style="width:7%">삭제</th>
                                                </tr>
                                            </thead>
                                            <tbody >
                                                <tr>

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
        </div>

    </div>
</template>

<script>
    import Left from "../../components/Left.vue";
    import Top from "../../components/Top.vue";

    export default {
        name: "User",
        components: {
            Left,
            Top
        },
        methods:{
            init:function () {
                setTimeout(function(){
                    $("#usertable").tablesorter({
                        headers:{
                            0:{sorter:'TextSort'},
                            1:{sorter:'TextSort'},
                            2:{sorter:false},
                            3:{sorter:'TextSort'},
                            4:{sorter:'TextSort'},
                            5:{sorter:'TextSort'},
                            6:{sorter:false}
                        }
                    });
                }, 500);
            }
        },
        mounted:function () {
            this.init();
        }
    }
</script>

<style scoped>

</style>
