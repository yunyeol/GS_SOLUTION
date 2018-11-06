<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left mail-menu-expand="false" mail-menu-show="collapse" settings-menu-active="active">
            </Left>
        </div>
        <div class="main-panel ps ps--active-y">
            <Top></Top>

            <div class="content">
                <div class="card" v-if="settList && settList.length > 0">
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
                                    <table id="datatable" class="table" role="grid" aria-describedby="datatable_info" style="width: 1091px;">
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
                                            <tr v-for="(item, index) in settList" :key="index">
                                                <td class="sorting_1 text-center">{{item.TYPE}}</td>
                                                <td class="text-center">{{item.TYPE}}</td>
                                                <td class="text-center">{{item.DATA1}}</td>
                                                <td class="text-center">{{item.DATA2}}</td>
                                                <td class="text-center">{{item.DATA3}}</td>
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
                    <!-- end content-->
                </div>
                <div class="card" v-else>
                    조회된 결과가 없습니다.
                </div>
                <!--  end card  -->
            </div>
        </div>

        <SettingsModal></SettingsModal>
    </div>
</template>

<script>
// @ is an alias to /src
import Left from "../components/Left.vue";
import Top from "../components/Top.vue";
import SettingsModal from "../components/settings/modal.vue";

export default {
  name: 'settings',
  components: {
      Left,
      Top,
      SettingsModal
  },
  data: function(){
      return {
          settList : []
      }
  },
  methods:{
    init : function () {
        $("#datatable").tablesorter();

        $('#datatable').DataTable({
            "pagingType": "full_numbers",
            "scrollY" : 300,
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
    getAxios: async function(){
        console.log(this.$API_URL+'/system/profile');
        const rv = await this.$axios({
            url: this.$API_URL+'/system/profile',
            method: 'get',
            timeout: 3000,
            headers: {
                'Content-Type': 'application/json'
            }
        }).catch (err => console.error(err));
    
        if(rv && rv['data']) {
            this.settList = rv['data'];
        }
    }
  },
  mounted: function(){
      this.init();
      this.getAxios(); 
  }
}
</script>
