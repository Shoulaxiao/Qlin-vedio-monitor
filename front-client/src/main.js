import Vue from 'vue'
import App from './App'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
import util from '@/utils/utils.js';
import * as R from 'ramda';
import moment from 'moment'


Vue.prototype.$util=util;
Vue.prototype.$R = R;
Vue.prototype.$moment = moment

App.mpType = 'app'

const app = new Vue({
  ...App
})
app.$mount()
