<template>
  <div class="navbar">
    <h1>ToDo List</h1>
    <div v-if="showExit" class="left">
      <div class="user">Olá {{name}}, seja bem-vindo(a)!</div>
      <div v-on:click="logout" class="pointer">SAIR</div>
    </div>
  </div>
</template>
<script>
export default {
  name: "header",
  props: ["showExit"],
  methods: {
    logout: function () {
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      this.$router.push("/auth");
    },
  },
  data() {
    return {
      name: localStorage.username,
    };
  },
};
</script>
<style>
.left{
  display: flex;
  flex-direction: row;
}
.navbar {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  color: "#0C3661";
  font-weight: bolder;
  font-size: 22px;
  padding-left: 5%;
  padding-right: 5%;
}

.pointer {
  padding-left: 30px;
  cursor: pointer;
  color: darkred;
}
</style>