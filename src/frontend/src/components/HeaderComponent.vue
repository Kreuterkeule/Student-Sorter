<template>
  <div class="container">
    <h1>Student-Sorter {{ version }} - <a href="https://github.com/Kreuterkeule">Kreuterkeule</a></h1>
  </div>
</template>

<script>

export default {
  data() {
    return {

    };
  },

  props: ['version'],

  methods: {

  },

  mounted() {

  },
};

</script>

<style lang="scss" scoped>
.container {
  margin: 16px;
  padding: 16px;
  width: calc(100% - 64px);
  height: fit-content;
  display: flex;
  align-items: center;
  justify-content: center;
  letter-spacing: 8px;
  h1 {
    font-size: 30px;
    color: white;
  }
}
</style>
