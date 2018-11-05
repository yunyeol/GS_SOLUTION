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
                    <div class="card-body">
                        <div class="toolbar">
                            <!--        Here you can write extra buttons/actions for the toolbar              -->
                        </div>
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
                                <div class="col-sm-12">
                                    <table id="datatable" class="table table-striped dataTable dtr-inline collapsed" role="grid" aria-describedby="datatable_info" style="width: 1091px;">
                                        <thead>
                                            <tr role="row">
                                                <th class="sorting_asc" tabindex="0" aria-controls="datatable" rowspan="1" colspan="1" style="width: 50px;" aria-sort="ascending" >Name</th>
                                                <th class="sorting" tabindex="0" aria-controls="datatable" rowspan="1" colspan="1" style="width: 15px;">Position</th>
                                                <th class="sorting" tabindex="0" aria-controls="datatable" rowspan="1" colspan="1" style="width: 153px;" >Office</th>
                                                <th class="sorting" tabindex="0" aria-controls="datatable" rowspan="1" colspan="1" style="width: 69px;" >Age</th>
                                                <th class="" tabindex="0" aria-controls="datatable" rowspan="1" colspan="1" style="width: 153px; display: none;">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr role="row" class="odd">
                                                <td tabindex="0" class="sorting_1">Airi Satou</td>
                                                <td>Accountant</td>
                                                <td>Tokyo</td>
                                                <td>33</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm btn-neutral like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm btn-neutral  edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm btn-neutral remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="even">
                                                <td class="sorting_1" tabindex="0">Angelica Ramos</td>
                                                <td>Chief Executive Officer (CEO)</td>
                                                <td>London</td>
                                                <td>47</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm btn-neutral like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm btn-neutral  edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm btn-neutral remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="odd">
                                                <td tabindex="0" class="sorting_1">Ashton Cox</td>
                                                <td>Junior Technical Author</td>
                                                <td>San Francisco</td>
                                                <td>66</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm btn-neutral like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm btn-neutral  edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm btn-neutral remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="even">
                                                <td class="sorting_1" tabindex="0">Bradley Greer</td>
                                                <td>Software Engineer</td>
                                                <td>London</td>
                                                <td>41</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm btn-neutral like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm btn-neutral  edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm btn-neutral remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="odd">
                                                <td class="sorting_1" tabindex="0">Brenden Wagner</td>
                                                <td>Software Engineer</td>
                                                <td>San Francisco</td>
                                                <td>28</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm btn-neutral like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm btn-neutral  edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm btn-neutral remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="even">
                                                <td tabindex="0" class="sorting_1">Brielle Williamson</td>
                                                <td>Integration Specialist</td>
                                                <td>New York</td>
                                                <td>61</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="odd">
                                                <td class="sorting_1" tabindex="0">Caesar Vance</td>
                                                <td>Pre-Sales Support</td>
                                                <td>New York</td>
                                                <td>21</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="even">
                                                <td tabindex="0" class="sorting_1">Cedric Kelly</td>
                                                <td>Senior Javascript Developer</td>
                                                <td>Edinburgh</td>
                                                <td>22</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="odd">
                                                <td class="sorting_1" tabindex="0">Charde Marshall</td>
                                                <td>Regional Director</td>
                                                <td>San Francisco</td>
                                                <td>36</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm remove"><i class="tim-icons icon-simple-remove"></i></a>
                                                </td>
                                            </tr>
                                            <tr role="row" class="even">
                                                <td tabindex="0" class="sorting_1">Colleen Hurst</td>
                                                <td>Javascript Developer</td>
                                                <td>San Francisco</td>
                                                <td>39</td>
                                                <td class="text-right" style="display: none;">
                                                    <a href="javascript:void(0)" class="btn btn-link btn-info btn-icon btn-sm like"><i class="tim-icons icon-heart-2"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-warning btn-icon btn-sm edit"><i class="tim-icons icon-pencil"></i></a>
                                                    <a href="javascript:void(0)" class="btn btn-link btn-danger btn-icon btn-sm remove"><i class="tim-icons icon-simple-remove"></i></a>
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
  }
}
</script>
