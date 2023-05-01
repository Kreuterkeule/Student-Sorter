<template>
  <div id="app">
    <HeaderComponent v-bind:version="this.version"></HeaderComponent>
    <router-view/>
  </div>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';

export default {
  data() {
    return {
      version: 'v.1.0',
    };
  },
  components: { HeaderComponent },
};
</script>

<style lang="scss">
body {
  background-color: #222;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: white;
}
label {
  display: flex;
  flex-direction: column;
}
a {
  color: lightgray;
  &:hover{
    color: rgb(88, 0, 77);
  }
}
.green {
  color: greenyellow;
}
.orange {
  color: orange;
}
</style>
