<template>
  <div id="home">
    <Header :showExit="true"/>
    <container />
  </div>
</template>

<script>
import container from './includes/container.vue'
import Header from "./includes/header.vue";

export default {
  name:'Home',
  components: {
    container,
    Header,
  }
}
</script>

<style lang="css" scoped>

</style>