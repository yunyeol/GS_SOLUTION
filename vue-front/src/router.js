import Vue from 'vue'
import Router from 'vue-router'

//로그인
import Login from './views/Login.vue'
import Profile from './views/Profile.vue'

//메일
import MailDashboard from './views/mail/Dashboard.vue'
import MailReceiver from './views/mail/Receiver.vue'
import MailSend from './views/mail/Send.vue'

//세팅
import Settings from './views/settings/System.vue'

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    //로그인 라우팅
    { path: '/login', name: 'login', component: Login},
    { path: '/profile', name: 'profile', component: Profile},

    //메일 라우팅
    { path: '/mail/dashboard', name: 'mail dashboard', component: MailDashboard},
    { path: '/mail/receiver', name: 'mail receiver', component: MailReceiver},
    { path: '/mail/send', name: 'mail send', component: MailSend},

    //푸시 라우팅

    //시스템코드 라우팅
    { path: '/settings/system', name: 'page settings', component: Settings}


      // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    // },
  ]
})
