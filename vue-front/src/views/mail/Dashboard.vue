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
                    <div class="col-12">
                        <div class="card card-chart">
                            <div class="card-header">
                                <div class="row">
                                    <div class="col-sm-6 text-left">
                                        <h5 class="card-category">Total Shipments</h5>
                                        <h2 class="card-title">Performance</h2>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="btn-group btn-group-toggle float-right" data-toggle="buttons">
                                            <label class="btn btn-sm btn-primary btn-simple active" id="0">
                                                <input type="radio" name="options" checked="">
                                                <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Accounts</span>
                                                <span class="d-block d-sm-none">
                          <i class="tim-icons icon-single-02"></i>
                        </span>
                                            </label>
                                            <label class="btn btn-sm btn-primary btn-simple" id="1">
                                                <input type="radio" class="d-none d-sm-none" name="options">
                                                <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Purchases</span>
                                                <span class="d-block d-sm-none">
                          <i class="tim-icons icon-gift-2"></i>
                        </span>
                                            </label>
                                            <label class="btn btn-sm btn-primary btn-simple" id="2">
                                                <input type="radio" class="d-none" name="options">
                                                <span class="d-none d-sm-block d-md-block d-lg-block d-xl-block">Sessions</span>
                                                <span class="d-block d-sm-none">
                          <i class="tim-icons icon-tap-02"></i>
                        </span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="chart-area"><div class="chartjs-size-monitor" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;"><div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div></div><div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:200%;height:200%;left:0; top:0"></div></div></div>
                                    <canvas id="chartBig1" width="1665" height="330" class="chartjs-render-monitor" style="display: block; height: 220px; width: 1110px;"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-6">
                        <div class="card card-stats">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-5">
                                        <div class="info-icon text-center icon-warning">
                                            <i class="tim-icons icon-chat-33"></i>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="numbers">
                                            <p class="card-category">Number</p>
                                            <h3 class="card-title">150GB</h3>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <hr>
                                <div class="stats">
                                    <i class="tim-icons icon-refresh-01"></i> Update Now
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-6">
                        <div class="card card-stats">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-5">
                                        <div class="info-icon text-center icon-primary">
                                            <i class="tim-icons icon-shape-star"></i>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="numbers">
                                            <p class="card-category">Followers</p>
                                            <h3 class="card-title">+45k</h3>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <hr>
                                <div class="stats">
                                    <i class="tim-icons icon-sound-wave"></i> Last Research
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-6">
                        <div class="card card-stats">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-5">
                                        <div class="info-icon text-center icon-success">
                                            <i class="tim-icons icon-single-02"></i>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="numbers">
                                            <p class="card-category">Users</p>
                                            <h3 class="card-title">150,000</h3>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <hr>
                                <div class="stats">
                                    <i class="tim-icons icon-trophy"></i> Customers feedback
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-6">
                        <div class="card card-stats">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-5">
                                        <div class="info-icon text-center icon-danger">
                                            <i class="tim-icons icon-molecule-40"></i>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="numbers">
                                            <p class="card-category">Errors</p>
                                            <h3 class="card-title">12</h3>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <hr>
                                <div class="stats">
                                    <i class="tim-icons icon-watch-time"></i> In the last hours
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--<div class="col-md-12 mb-5">-->
                        <!--<div class="card">-->
                            <!--<div class="card-header">-->
                                <!--<h4 class="card-title"> Simple Table</h4>-->
                            <!--</div>-->
                            <!--<div class="card-body">-->
                                <!--<div class="table-responsive ps">-->
                                    <!--<table class="table tablesorter " id="simple-table">-->
                                        <!--<thead class=" text-primary">-->
                                        <!--<tr>-->
                                            <!--<th class="header">-->
                                                <!--Name-->
                                            <!--</th>-->
                                            <!--<th class="header">-->
                                                <!--Country-->
                                            <!--</th>-->
                                            <!--<th class="header">-->
                                                <!--City-->
                                            <!--</th>-->
                                            <!--<th class="text-center header">-->
                                                <!--Salary-->
                                            <!--</th>-->
                                        <!--</tr>-->
                                        <!--</thead>-->
                                        <!--<tbody>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Dakota Rice-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Niger-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Oud-Turnhout-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$36,738-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Minerva Hooper-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Curaçao-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Sinaai-Waas-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$23,789-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Sage Rodriguez-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Netherlands-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Baileux-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$56,142-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Philip Chaney-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Korea, South-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Overland Park-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$38,735-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Doris Greene-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Malawi-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Feldkirchen in Kärnten-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$63,542-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Mason Porter-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Chile-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Gloucester-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$78,615-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--<tr>-->
                                            <!--<td>-->
                                                <!--Jon Porter-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Portugal-->
                                            <!--</td>-->
                                            <!--<td>-->
                                                <!--Gloucester-->
                                            <!--</td>-->
                                            <!--<td class="text-center">-->
                                                <!--$98,615-->
                                            <!--</td>-->
                                        <!--</tr>-->
                                        <!--</tbody>-->
                                    <!--</table>-->
                                    <!--<div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>-->
                            <!--</div>-->
                        <!--</div>-->
                    <!--</div>-->

                    <div class="col-md-12 mb-5">
                        <div class="card">
                            <div class="card-header">
                                <div class="tools float-right">
                                    <div class="dropdown">
                                        <button type="button" class="btn btn-link dropdown-toggle btn-icon" data-toggle="dropdown">
                                            <i class="tim-icons icon-settings-gear-63"></i>
                                        </button>
                                        <div class="dropdown-menu dropdown-menu-right">
                                            <a class="dropdown-item" href="#pablo">Action</a>
                                            <a class="dropdown-item" href="#pablo">Another action</a>
                                            <a class="dropdown-item" href="#pablo">Something else</a>
                                            <a class="dropdown-item text-danger" href="#pablo">Remove Data</a>
                                        </div>
                                    </div>
                                </div>
                                <h5 class="card-title">Management Table</h5>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive ps">
                                    <table class="table">
                                        <thead class="text-primary">
                                        <tr>
                                            <th class="text-center">
                                                #
                                            </th>
                                            <th>
                                                Name
                                            </th>
                                            <th>
                                                Job Position
                                            </th>
                                            <th>
                                                Milestone
                                            </th>
                                            <th class="text-right">
                                                Salary
                                            </th>
                                            <th class="text-right">
                                                Actions
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td class="text-center">
                                                <div class="photo">
                                                    <img src="../../assets/img/tania.jpg" alt="photo">
                                                </div>
                                            </td>
                                            <td>
                                                Tania Mike
                                            </td>
                                            <td>
                                                Develop
                                            </td>
                                            <td class="text-center">
                                                <div class="progress-container progress-sm">
                                                    <div class="progress">
                                                        <span class="progress-value">25%</span>
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 25%;"></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-right">
                                                € 99,225
                                            </td>
                                            <td class="text-right">
                                                <button type="button" rel="tooltip" class="btn btn-success btn-link btn-icon btn-sm btn-neutral" data-original-title="Refresh" title="">
                                                    <i class="tim-icons icon-refresh-01"></i>
                                                </button>
                                                <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-icon btn-sm btn-neutral" data-original-title="Delete" title="">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">
                                                <div class="photo">
                                                    <img src="../../assets/img/robi.jpg" alt="photo">
                                                </div>
                                            </td>
                                            <td>
                                                John Doe
                                            </td>
                                            <td>
                                                CEO
                                            </td>
                                            <td class="text-center">
                                                <div class="progress-container progress-sm">
                                                    <div class="progress">
                                                        <span class="progress-value">77%</span>
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 77%;"></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-right">
                                                € 89,241
                                            </td>
                                            <td class="text-right">
                                                <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm btn-icon btn-neutral" data-original-title="Refresh" title="">
                                                    <i class="tim-icons icon-refresh-01"></i>
                                                </button>
                                                <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon btn-neutral" data-original-title="Delete" title="">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">
                                                <div class="photo">
                                                    <img src="../../assets/img/lora.jpg" alt="photo">
                                                </div>
                                            </td>
                                            <td>
                                                Alexa Mike
                                            </td>
                                            <td>
                                                Design
                                            </td>
                                            <td class="text-center">
                                                <div class="progress-container progress-sm">
                                                    <div class="progress">
                                                        <span class="progress-value">41%</span>
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 41%;"></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-right">
                                                € 92,144
                                            </td>
                                            <td class="text-right">
                                                <button type="button" rel="tooltip" class="btn btn-success btn-link btn-icon btn-sm btn-neutral" data-original-title="Refresh" title="">
                                                    <i class="tim-icons icon-refresh-01"></i>
                                                </button>
                                                <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-icon btn-sm btn-neutral" data-original-title="Delete" title="">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">
                                                <div class="photo">
                                                    <img src="../../assets/img/jana.jpg" alt="photo">
                                                </div>
                                            </td>
                                            <td>
                                                Jana Monday
                                            </td>
                                            <td>
                                                Marketing
                                            </td>
                                            <td class="text-center">
                                                <div class="progress-container progress-sm">
                                                    <div class="progress">
                                                        <span class="progress-value">50%</span>
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 50%;"></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-right">
                                                € 49,990
                                            </td>
                                            <td class="text-right">
                                                <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm btn-icon" data-original-title="Refresh" title="">
                                                    <i class="tim-icons icon-refresh-01"></i>
                                                </button>
                                                <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon" data-original-title="Delete" title="">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">
                                                <div class="photo">
                                                    <img src="../../assets/img/mike.jpg" alt="photo">
                                                </div>
                                            </td>
                                            <td>
                                                Paul Dickens
                                            </td>
                                            <td>
                                                Develop
                                            </td>
                                            <td class="text-center">
                                                <div class="progress-container progress-sm">
                                                    <div class="progress">
                                                        <span class="progress-value">100%</span>
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;">
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-right">
                                                € 69,201
                                            </td>
                                            <td class="text-right">
                                                <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm  btn-icon" data-original-title="Refresh" title="">
                                                    <i class="tim-icons icon-refresh-01"></i>
                                                </button>
                                                <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon" data-original-title="Delete" title="">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="text-center">
                                                <div class="photo">
                                                    <img src="../../assets/img/emilyz.jpg" alt="photo">
                                                </div>
                                            </td>
                                            <td>
                                                Manuela Rico
                                            </td>
                                            <td>
                                                Manager
                                            </td>
                                            <td class="text-center">
                                                <div class="progress-container progress-sm">
                                                    <div class="progress">
                                                        <span class="progress-value">15%</span>
                                                        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 15%;"></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-right">
                                                € 99,201
                                            </td>
                                            <td class="text-right">
                                                <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm btn-icon" data-original-title="Refresh" title="">
                                                    <i class="tim-icons icon-refresh-01"></i>
                                                </button>
                                                <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon" data-original-title="Delete" title="">
                                                    <i class="tim-icons icon-simple-remove"></i>
                                                </button>
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
