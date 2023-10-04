<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: "App"
};
</script>

<style>
@import url("https://fonts.googleapis.com/css?family=Quicksand:400,500,700");
html {
  scroll-behavior: smooth;
}
#app {
  font-family: "Quicksand", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: '#0C3661';
  background-color: #EFF2F7;
  min-height: 100vh;
}

#apps {
  list-style-type: none;
  margin-top: 5vh;
  flex: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-wrap: wrap;
  padding-left: 40px;
  padding-right: 40px;
}

body::-webkit-scrollbar {
  width: 7px; /* width of the entire scrollbar */
}

body::-webkit-scrollbar-track {
  background: #2f2f2f; /* color of the tracking area */
}

body::-webkit-scrollbar-thumb {
  background-color: rgb(70, 70, 69); /* color of the scroll thumb */
  border-radius: 20px; /* roundness of the scroll thumb */
  border: 3px solid rgb(70, 70, 69); /* creates padding around scroll thumb */
}
</style>
