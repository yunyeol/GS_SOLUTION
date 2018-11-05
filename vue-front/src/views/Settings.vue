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
                                <h4 class="card-title" style="float:left;"> Settings </h4>
                                <div style="width:20px;float:left;">&nbsp;</div>
                                <button style="float:left;" class="btn btn-primary btn-sm tim-icons icon-simple-add" onclick="showSwal('input-field')"></button>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive ps">
                                    <table class="table tablesorter " id="settings">
                                        <thead class=" text-primary">
                                            <tr>
                                                <th class="text-center header">타입</th>
                                                <th class="text-center header">구분</th>
                                                <th class="text-center header">데이터1</th>
                                                <th class="text-center header">데이터2</th>
                                                <th class="text-center header">데이터3</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>Dakota Rice</td>
                                            <td>Niger</td>
                                            <td>Oud-Turnhout</td>
                                            <td class="text-center">$36,738</td>
                                        </tr>
                                        <tr>
                                            <td>Minerva Hooper</td>
                                            <td>Curaçao</td>
                                            <td>Sinaai-Waas</td>
                                            <td class="text-center">$23,789</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
                            </div>

                            <div class="card-body">
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination">
                                        <li class="page-item">
                                            <a class="page-link" href="#link" aria-label="Previous">
                                                <span aria-hidden="true"><i class="tim-icons icon-double-left" aria-hidden="true"></i></span>
                                            </a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#link">1</a>
                                        </li>
                                        <li class="page-item active">
                                            <a class="page-link" href="#link">2</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#link">3</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="#link" aria-label="Next">
                                                <span aria-hidden="true"><i class="tim-icons icon-double-right" aria-hidden="true"></i></span>
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
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
         mailMeunActive : ''
        ,mailSubMenuActive1 : ''
        ,mailSubMenuActive2 : ''
        ,mailSubMenuActive3 : ''
        ,mailMenuExpand : 'false'
        ,mailMenuShow : 'collapse'
        ,settingsMenuActive : 'active'
    }
  }
}
</script>
