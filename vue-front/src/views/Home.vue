<template>
    <div class="wrapper">
        <div class="sidebar">
            <Left></Left>
        </div>
        <div class="main-panel ps ps--active-y">
            <Top></Top>


        </div>
    </div>
</template>

<script>
// @ is an alias to /src
import Left from "../components/Left.vue";
import Top from "../components/Top.vue";

export default {
  name: 'home',
    components: {
      Left,
      Top
  }
}
</script>
