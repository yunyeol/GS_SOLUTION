<template>
    <div class="sidebar-wrapper ps ps--active-x ps--active-y">
        <div class="logo">
            <a href="javascript:void(0)" class="simple-text logo-mini">
                Al
            </a>
            <a href="javascript:void(0)" class="simple-text logo-normal">
                Alimi
            </a>
        </div>
        <ul class="nav">
            <li v-bind:class="mailMenuActive">
                <a data-toggle="collapse" href="#mailPage" class="" v-bind:aria-expanded="mailMenuExpand">
                    <i class="tim-icons icon-email-85"></i>
                    <p>
                        mail
                        <b class="caret"></b>
                    </p>
                </a>
                <div v-bind:class="mailMenuShow" id="mailPage" style="">
                    <ul class="nav">
                        <li v-bind:class="mailSubMenuActive1">
                            <router-link to="/mail/dashboard">
                                <span class="sidebar-mini-icon">D</span>
                                <span class="sidebar-normal"> dashboard </span>
                            </router-link>
                        </li>
                        <li v-bind:class="mailSubMenuActive2">
                            <router-link to="/mail/receiver">
                                <span class="sidebar-mini-icon">R</span>
                                <span class="sidebar-normal"> receiver </span>
                            </router-link>
                        </li>
                        <li v-bind:class="mailSubMenuActive3">
                            <router-link to="/mail/send">
                                <span class="sidebar-mini-icon">S</span>
                                <span class="sidebar-normal"> send </span>
                            </router-link>
                        </li>
                    </ul>
                </div>
            </li>
            <!--<li class="">-->
                <!--<a data-toggle="collapse" href="" class="" aria-expanded="false">-->
                    <!--<i class="tim-icons icon-tablet-2"></i>-->
                    <!--<p>-->
                        <!--push-->
                        <!--<b class="caret"></b>-->
                    <!--</p>-->
                <!--</a>-->
                <!--<div class="collapse" id="" style="">-->
                    <!--<ul class="nav">-->
                        <!--<li>-->
                            <!--<a href="../examples/pages/pricing.html">-->
                                <!--<span class="sidebar-mini-icon">P</span>-->
                                <!--<span class="sidebar-normal"> dashboard </span>-->
                            <!--</a>-->
                        <!--</li>-->
                        <!--<li>-->
                            <!--<a href="../examples/pages/rtl.html">-->
                                <!--<span class="sidebar-mini-icon">RS</span>-->
                                <!--<span class="sidebar-normal"> receiver </span>-->
                            <!--</a>-->
                        <!--</li>-->
                        <!--<li>-->
                            <!--<a href="../examples/pages/timeline.html">-->
                                <!--<span class="sidebar-mini-icon">T</span>-->
                                <!--<span class="sidebar-normal"> send </span>-->
                            <!--</a>-->
                        <!--</li>-->
                    <!--</ul>-->
                <!--</div>-->
            <!--</li>-->
            <li v-bind:class="settingsMenuActive">
                <a data-toggle="collapse" href="#settingsPage" class="" v-bind:aria-expanded="settingsMenuExpand">
                    <i class="tim-icons icon-settings-gear-63"></i>
                    <p>
                        settings
                        <b class="caret"></b>
                    </p>
                </a>
                <div v-bind:class="settingsMenuShow" id="settingsPage" style="">
                    <ul class="nav">
                        <li v-bind:class="settingsSubMenuActive1">
                            <router-link to="/settings/system">
                                <span class="sidebar-mini-icon">S</span>
                                <span class="sidebar-normal"> system </span>
                            </router-link>
                        </li>
                        <li v-bind:class="settingsSubMenuActive2">
                            <router-link to="/settings/user">
                                <span class="sidebar-mini-icon">U</span>
                                <span class="sidebar-normal"> user </span>
                            </router-link>
                        </li>
                    </ul>
                </div>
            </li>

            <!--<li>-->
                <!--<a href="./icons.html">-->
                    <!--<i class="tim-icons icon-atom"></i>-->
                    <!--<p>Icons</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li>-->
                <!--<a href="./map.html">-->
                    <!--<i class="tim-icons icon-pin"></i>-->
                    <!--<p>Maps</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li>-->
                <!--<a href="./notifications.html">-->
                    <!--<i class="tim-icons icon-bell-55"></i>-->
                    <!--<p>Notifications</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li>-->
                <!--<a href="./user.html">-->
                    <!--<i class="tim-icons icon-single-02"></i>-->
                    <!--<p>User Profile</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li>-->
                <!--<a href="./tables.html">-->
                    <!--<i class="tim-icons icon-puzzle-10"></i>-->
                    <!--<p>Table List</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li>-->
                <!--<a href="./typography.html">-->
                    <!--<i class="tim-icons icon-align-center"></i>-->
                    <!--<p>Typography</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li>-->
                <!--<a href="./rtl.html">-->
                    <!--<i class="tim-icons icon-world"></i>-->
                    <!--<p>RTL Support</p>-->
                <!--</a>-->
            <!--</li>-->
            <!--<li class="active-pro">-->
                <!--<a href="./upgrade.html">-->
                    <!--<i class="tim-icons icon-spaceship"></i>-->
                    <!--<p>Upgrade to PRO</p>-->
                <!--</a>-->
            <!--</li>-->
        </ul>
        <div class="ps__rail-x" style="left: 0px; bottom: 0px;">
            <div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div>
        </div>
        <div class="ps__rail-y" style="top: 0px; height: 471px; right: 0px;">
            <div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 401px;"></div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Left"
        ,props: [
             'mailMenuActive'
            ,'mailSubMenuActive1'
            ,'mailSubMenuActive2'
            ,'mailSubMenuActive3'
            ,'mailMenuExpand'
            ,'mailMenuShow'

            ,'settingsMenuActive'
            ,'settingsMenuExpand'
            ,'settingsMenuShow'
            ,'settingsSubMenuActive1'
            ,'settingsSubMenuActive2'
        ]
        ,data: function(){
            return {

            }
        }
    }
</script>

<style scoped>

</style>
