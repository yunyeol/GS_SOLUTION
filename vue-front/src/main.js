import Vue from 'vue'
import BootstrapVue from "bootstrap-vue"
import App from './App.vue'
import router from './router.js'
import axios from 'axios'

Vue.use(BootstrapVue);

Vue.config.productionTip = false;

Vue.prototype.$axios = axios;
//Vue.prototype.$router = router;
Vue.prototype.$API_URL = 'http://127.0.0.1:10009/api';

new Vue({
  el: '#app',
  router,
  render: h => h(App)
});
