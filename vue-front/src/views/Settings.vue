<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left v-bind:mail-menu-active="mailMeunActive"
                  v-bind:mail-sub-menu-active1="mailSubMenuActive1"
                  v-bind:mail-sub-menu-active2="mailSubMenuActive2"
                  v-bind:mail-sub-menu-active3="mailSubMenuActive3"
                  v-bind:mail-menu-expand="mailMenuExpand"
                  v-bind:mail-menu-show="mailMenuShow"
                  v-bind:settings-menu-active="settingsMenuActive">
            </Left>
        </div>
        <div class="main-panel ps ps--active-y">
            <Top></Top>

            <div class="content">
                <div class="card">
                    <div class="card-header">
                        <div class="row">
                            <h4 class="col-sm-11">
                                Settings
                            </h4>
                            <div class="col-sm-1">
                                <button style="float:right;" class="btn btn-primary btn-sm tim-icons icon-simple-add" onclick="showSwal('input-field')"></button>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div id="datatable_wrapper" class="dataTables_wrapper dt-bootstrap4">
                            <div class="row">
                                <div class="col-sm-12 col-md-6">
                                    <div class="dataTables_length" id="datatable_length1">
                                        <label>Show
                                            <select name="datatable_length" aria-controls="datatable" class="custom-select custom-select-sm form-control form-control-sm">
                                                <option value="10">10</option>
                                                <option value="25">25</option>
                                                <option value="50">50</option>
                                                <option value="-1">All</option>
                                            </select> entries
                                        </label>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-6">
                                    <div id="datatable_filter" class="dataTables_filter">
                                        <label>
                                            <input type="search" class="form-control form-control-sm" placeholder="Search records" aria-controls="datatable">
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="table-responsive ps">
                                    <table id="datatable" class="table tablesorter" role="grid" aria-describedby="datatable_info" style="width: 1091px;">
                                        <thead>
                                            <tr role="row">
                                                <th class="sorting_asc text-center header" tabindex="0" aria-controls="datatable" style="width: 60px;" >타입</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable" style="width: 60px;">구분</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable"  >데이터1</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable"  >데이터2</th>
                                                <th class="sorting text-center header" tabindex="0" aria-controls="datatable">데이터3</th>
                                                <th class="text-center header" tabindex="0" aria-controls="datatable"  style="width: 20px;">&nbsp;</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr >
                                                <td class="sorting_1 text-center">0000</td>
                                                <td class="text-center">0000</td>
                                                <td class="text-center">Tokyo</td>
                                                <td class="text-center">33</td>
                                                <td class="text-center"></td>
                                                <td class="text-center">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </td>
                                            </tr>
                                            <tr >
                                                <td class="sorting_1 text-center">0000</td>
                                                <td class="text-center">0001</td>
                                                <td class="text-center">abco</td>
                                                <td class="text-center">34</td>
                                                <td class="text-center">1</td>
                                                <td class="text-center">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12 col-md-5">
                                    <div class="dataTables_info" id="datatable_info" role="status" aria-live="polite">
                                        Showing 1 to 10 of 42 entries
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-7">
                                    <div class="dataTables_paginate paging_full_numbers" id="datatable_paginate">
                                        <ul class="pagination">
                                            <li class="paginate_button page-item first disabled" id="datatable_first">
                                                <a href="#" aria-controls="datatable" data-dt-idx="0" tabindex="0" class="page-link">First</a>
                                            </li>
                                            <li class="paginate_button page-item previous disabled" id="datatable_previous">
                                                <a href="#" aria-controls="datatable" data-dt-idx="1" tabindex="0" class="page-link">Previous</a>
                                            </li>
                                            <li class="paginate_button page-item active">
                                                <a href="#" aria-controls="datatable" data-dt-idx="2" tabindex="0" class="page-link">1</a>
                                            </li>
                                            <li class="paginate_button page-item ">
                                                <a href="#" aria-controls="datatable" data-dt-idx="3" tabindex="0" class="page-link">2</a>
                                            </li>
                                            <li class="paginate_button page-item ">
                                                <a href="#" aria-controls="datatable" data-dt-idx="4" tabindex="0" class="page-link">3</a>
                                            </li>
                                            <li class="paginate_button page-item ">
                                                <a href="#" aria-controls="datatable" data-dt-idx="5" tabindex="0" class="page-link">4</a>
                                            </li>
                                            <li class="paginate_button page-item ">
                                                <a href="#" aria-controls="datatable" data-dt-idx="6" tabindex="0" class="page-link">5</a>
                                            </li>
                                            <li class="paginate_button page-item next" id="datatable_next">
                                                <a href="#" aria-controls="datatable" data-dt-idx="7" tabindex="0" class="page-link">Next</a>
                                            </li>
                                            <li class="paginate_button page-item last" id="datatable_last">
                                                <a href="#" aria-controls="datatable" data-dt-idx="8" tabindex="0" class="page-link">Last</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- end content-->
                </div>
                <!--  end card  -->
            </div>
        </div>
    </div>
</template>

<script>
// @ is an alias to /src
import Left from "../components/Left.vue";
import Top from "../components/Top.vue";

export default {
  name: 'settings',
    components: {
      Left,
      Top
  },
  data: function(){
      return {
        mailMeunActive : '',
        mailSubMenuActive1 : '',
        mailSubMenuActive2 : '',
        mailSubMenuActive3 : '',
        mailMenuExpand : 'false',
        mailMenuShow : 'collapse',

        settingsMenuActive : 'active'
    }
  },
  methods:{
    init : function () {
        $("#datatable").tablesorter();
    }
  },
  mounted: function(){
      this.init();
  }
}
</script>
