<template>
  <div class="home">
    <AmChart/>
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
// @ is an alias to /src
import Vue from 'vue'
import HelloWorld from '@/components/HelloWorld.vue'
import AmChart from '@/components/AmChart.vue'

Vue.component('HelloWorld', HelloWorld);
Vue.component('AmChart', AmChart);

export default {
  name: 'home'
  // ,
  // components: {
  //   HelloWorld
  // }
}
</script>
