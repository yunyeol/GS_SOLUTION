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
                <div class="row">
                    <div class="col-md-12 mb-5">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title"> Simple Table</h4>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive ps">
                                    <table class="table tablesorter " id="simple-table">
                                        <thead class=" text-primary">
                                        <tr>
                                            <th class="header">
                                                Name
                                            </th>
                                            <th class="header">
                                                Country
                                            </th>
                                            <th class="header">
                                                City
                                            </th>
                                            <th class="text-center header">
                                                Salary
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>
                                                Dakota Rice
                                            </td>
                                            <td>
                                                Niger
                                            </td>
                                            <td>
                                                Oud-Turnhout
                                            </td>
                                            <td class="text-center">
                                                $36,738
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Minerva Hooper
                                            </td>
                                            <td>
                                                Curaçao
                                            </td>
                                            <td>
                                                Sinaai-Waas
                                            </td>
                                            <td class="text-center">
                                                $23,789
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Sage Rodriguez
                                            </td>
                                            <td>
                                                Netherlands
                                            </td>
                                            <td>
                                                Baileux
                                            </td>
                                            <td class="text-center">
                                                $56,142
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Philip Chaney
                                            </td>
                                            <td>
                                                Korea, South
                                            </td>
                                            <td>
                                                Overland Park
                                            </td>
                                            <td class="text-center">
                                                $38,735
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Doris Greene
                                            </td>
                                            <td>
                                                Malawi
                                            </td>
                                            <td>
                                                Feldkirchen in Kärnten
                                            </td>
                                            <td class="text-center">
                                                $63,542
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Mason Porter
                                            </td>
                                            <td>
                                                Chile
                                            </td>
                                            <td>
                                                Gloucester
                                            </td>
                                            <td class="text-center">
                                                $78,615
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Jon Porter
                                            </td>
                                            <td>
                                                Portugal
                                            </td>
                                            <td>
                                                Gloucester
                                            </td>
                                            <td class="text-center">
                                                $98,615
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
// @ is an alias to /src
import Left from "../../components/Left.vue";
import Top from "../../components/Top.vue";

export default {
  name: 'dashboard',
  components: {
      Left,
      Top
  },
  data: function(){
      var param = {
          mailMeunActive : 'active',
          mailSubMenuActive1 : 'active',
          mailSubMenuActive2 : '',
          mailSubMenuActive3 : '',
          mailMenuExpand : 'true',
          mailMenuShow : 'collapse show',

          settingsMenuActive : ''
      };

      return param;
  }
}
</script>
