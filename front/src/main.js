import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import moment from 'moment'
import VModal from 'vue-js-modal'
import Header from '@/components/common/Header.vue'
import Footer from '@/components/common/Footer.vue'

Vue.use(VModal, { dynamic: true, dialog : true })

Vue.prototype.$axios = axios
Vue.prototype.$moment = moment
Vue.prototype.$API_URL = 'http://127.0.0.1/api'

Vue.component('Header', Header);
Vue.component('Footer', Footer);

Vue.config.productionTip = false

console.log(process.env.NODE_ENV);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
