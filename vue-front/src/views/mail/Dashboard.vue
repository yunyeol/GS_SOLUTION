<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left v-bind:mail-menu-active="mailMeunActive"
                  v-bind:mail-sub-menu-active1="mailSubMenuActive1"
                  v-bind:mail-sub-menu-active2="mailSubMenuActive2"
                  v-bind:mail-sub-menu-active3="mailSubMenuActive3"
                  v-bind:mail-menu-expand="mailMenuExpand"
                  v-bind:mail-menu-show="mailMenuShow"
                  v-bind:settings-menu-active="settingsMenuActive"

                  settings-menu-active="" settings-menu-expand="false" settings-menu-show="collapse" settings-sub-menu-active1="" settings-sub-menu-active2="">
            </Left>
        </div>
        <div class="main-panel ps ps--active-y">
            <Top title="mail dashboard"></Top>

            <div class="content">
                <div class="row">
                    <!--chart-->
                    <div class="col-12">
                        <TopChart></TopChart>
                    </div>

                    <DailyCountTotal></DailyCountTotal>
                    <DailyCountSuccess></DailyCountSuccess>
                    <DailyCountFail></DailyCountFail>
                    <DailyCountRead></DailyCountRead>

                    <div class="col-md-12 mb-5">
                        <DashBoardList></DashBoardList>
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

import TopChart from "../../components/mail/dashboard/TopChart.vue";
import DailyCountTotal from "../../components/mail/dashboard/DailyCountTotal.vue";
import DailyCountSuccess from "../../components/mail/dashboard/DailyCountSuccess.vue";
import DailyCountFail from "../../components/mail/dashboard/DailyCountFail.vue";
import DailyCountRead from "../../components/mail/dashboard/DailyCountRead.vue";
import DashBoardList from "../../components/mail/dashboard/DashBoardList.vue";

export default {
  name: 'dashboard',
  components: {
       Left
      ,Top
      ,TopChart
      ,DailyCountTotal
      ,DailyCountSuccess
      ,DailyCountFail
      ,DailyCountRead
      ,DashBoardList
  },
  data: function(){
      return {
           mailMeunActive : 'active'
          ,mailSubMenuActive1 : 'active'
          ,mailSubMenuActive2 : ''
          ,mailSubMenuActive3 : ''
          ,mailMenuExpand : 'true'
          ,mailMenuShow : 'collapse show'
          ,settingsMenuActive : ''
      }
  },
  methods: {

  }
}
</script>
