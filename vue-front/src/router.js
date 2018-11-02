import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    //로그인 라우팅
    { path: '/login', name: 'login', component: Login},

    //메일 라우팅
    { path: '/mail/dashboard', name: 'mail dashboard', component: Home},
    { path: '/mail/receiver', name: 'mail receiver', component: Home},
    { path: '/mail/send', name: 'mail receiver', component: Home},

    //푸시 라우팅

    //시스템코드 라우팅
    { path: '/settings', name: 'page settings', component: Home}


      // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    // },
    // {
    //   path: '/todo',
    //   name: 'todo',
    //   component: () => import(/* webpackChunkName: "about" */ './views/TodoPage.vue')
    // },
    // {
    //   path: '/amchart',
    //   name: 'amchart',
    //   component: () => import(/* webpackChunkName: "about" */ './components/AmChart.vue')
    // }
  ]
})
