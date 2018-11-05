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
                        send
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
  name: 'send',
    components: {
      Left,
      Top
  },
  data: function(){
    return {
        mailMeunActive : 'active'
        ,mailSubMenuActive1 : ''
        ,mailSubMenuActive2 : ''
        ,mailSubMenuActive3 : 'active'
        ,mailMenuExpand : 'true'
        ,mailMenuShow : 'collapse show'
        ,settingsMenuActive : ''
    }
  }
}
</script>
