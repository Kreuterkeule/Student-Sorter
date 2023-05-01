<!-- eslint-disable max-len -->
<template>
  <div class="container">
    <div class="login-form" v-if="!loggedIn">
      <form>
        <h1>login</h1>
        <label for="username">Username
          <input type="text" name="username" id="username" v-model="login.username" placeholder="username" required>
        </label>
        <label for="password">Password
          <input type="password" name="password" id="password" v-model="login.password" placeholder="password" required>
        </label>
        <button type="submit" @click.prevent="handleLogin()">login</button>
      </form>
      <router-link to="/help">help</router-link>
    </div>
    <div class="change-user-data-form" v-else>
      <button @click.prevent="handleLogout()">logout</button>
      <div v-if="adminUser" class="admin-control">
        <h2>Admin Control</h2>
        <AdminControllComponent></AdminControllComponent>
      </div>
      <div v-else class="client-control">
        <h2>Welcome {{ username }}</h2>
      </div>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
/* eslint-disable max-len */
import BackendService from '../service/BackendService';
import AdminControllComponent from '../components/AdminControllComponent.vue';

export default {
  name: 'HomeView',
  data() {
    return {
      login: {
        username: '',
        password: '',
      },
    };
  },

  mounted() {
    console.log(`loggedIn? ${this.loggedIn}`);
    console.log(`adminUser?${this.adminUser}`);
  },

  methods: {
    handleLogout() {
      this.$store.commit('reset');
      this.$store.commit('setLoggedIn', false);
      this.$store.commit('setAdmin', false);
      this.$router.go();
    },
    async handleLogin() {
      await BackendService.getUserInfo(this.login.username, this.login.password)
        .then((response) => {
          if (!response.data.locked && !response.data.accExpired && !response.data.credExpired && response.data.enabled) {
            if (response.data.role === 'ADMIN') {
              console.log('admin user detected');
              this.$store.commit('setAdmin', true);
            }
            this.$store.commit('setLoggedIn', true);
            this.$store.commit('setUsername', response.data.username);
            this.$store.commit('setPassword', this.password); // no token since basic auth should do the trick for such application
            this.$router.go();
          }
        });
    },
  },

  computed: {
    adminUser() {
      return this.$store.state.userdata.isAdmin;
    },
    loggedIn() {
      return this.$store.state.loggedIn;
    },
    username() {
      return this.$store.state.userdata.username;
    },
  },

  components: {
    AdminControllComponent,
  },
};
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-form {
  max-width: 300px;
  height: fit-content;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  text-align: center;
  background-color: rgb(88, 0, 77);
  padding: 32px;
  border-radius: 16px;
  * {
    padding: 4px;
    margin: 8px;
  }
  button {
    background-color: rgb(228, 70, 220);
    border: none;
    padding: 8px 16px;
    border-radius: 4px;
    &:hover {
      background-color: lightgray;
      text-decoration: underline;
    }
  }
}
</style>
