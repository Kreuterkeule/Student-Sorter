<!-- eslint-disable max-len -->
<template>
  <div class="container">
    <h1>Student-Sorter {{ version }} - <a href="https://github.com/Kreuterkeule">Kreuterkeule</a></h1>
    <div v-if="messages.length > 0" class="notifications">
      <button @click.prevent="handleClear()">clear</button> else press ESC to clear
      <div :class="(message.type === 'good') ? 'good' : 'bad'" v-for="message of messages" :key="message.heading">
        <h1>{{ message.head }}</h1>
        <p>{{ message.text }}</p>
      </div>
    </div>
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
    handleClear() {
      this.$store.commit('resetNotifyMessages');
      this.$store.commit('setNotifications', []);
    },
  },

  mounted() {
    document.addEventListener('keydown', (event) => {
      const name = event.key;
      if (name === 'Escape') {
        this.handleClear();
      }
    });
  },

  computed: {
    messages() {
      return this.$store.state.notifyMessages;
    },
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
.notifications {
  letter-spacing: normal;
  max-height: 90vh;
  overflow-y: scroll;
  position: fixed;
  right: 20px;
  top: 20px;
  background-color: #222;
  border: 4px solid gray;
  padding: 8px 8px 8px 16px;
  > div {
    border-top: 4px solid gray;
    &:first-child {
      border-top: none;
    }
  }
  button {
    background-color: red;
    padding: 8px 16px;
    border: none;
    border-radius: 8px;
    margin: 16px;
    &:hover {
      background-color: orange;
      cursor: pointer;
    }
  }
}
.good {
  color: green;
  h1 {
    color: green;
  }
}
.bad {
  color: orange;
  h1 {
    color: orange;
  }
}
</style>
