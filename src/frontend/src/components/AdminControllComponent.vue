<!-- eslint-disable max-len -->
<template>
  <div class="container">
    <div class="create-users-form">
      <h2>Create Users</h2> <!-- maybe check user existance before committing needing a rest endpoint for that here -->
      <div class="admin-form">
        <label for="usernames">Usernames (seperated by ',' need to be unique f.e 'regine.teschke' passwords are generated automatically and need to be copied afterwards since just their hashes are stored for security reasons!)
          <input type="text" name="usernames" id="usernames" placeholder="usernames" required v-model="createUsers">
        </label>
        <button type="submit" @click="handleCreateUsers()">Create</button>
      </div>
      <div class="success-users" v-if="createUsersResponse.createUsersSuccess.length > 0">
        <h2 class="green">Success creating: </h2> <!-- show if list not empty -->
        <table>
          <tr>
            <th>username</th>
            <th>password</th>
            <th>role</th>
          </tr>
          <tr v-for="user of createUsersResponse.createUsersSuccess" :key="user.username">
            <td>{{ user.username }}</td>
            <td>{{ user.password }}</td>
            <td>{{ user.role }}</td>
          </tr>
        </table>
      </div>
      <div v-if="createUsersResponse.createUsersError.length > 0" class="error-users"> <!-- check if list empty -->
        <h2 class="orange">Errors creating: </h2>
        <table>
          <tr>
            <th>Error Type</th>
            <th>username</th>
            <th>password</th>
          </tr>
          <tr v-for="user of createUsersResponse.createUsersError" :key="user.username">
            <td>{{ user.errorType }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.password }}</td>
          </tr>
        </table>
      </div>
    </div>
    <div class="create-admin-form">
      <h2>Create Single User</h2>
      <form>
        <label for="username">
          <input type="text" name="username" id="username" v-model="createUser.username" required placeholder="username">
        </label>
        <label for="password">
          <input type="password" name="password" id="password" v-model="createUser.password" required placeholder="password">
        </label>
        <button @click.prevent="handleCreateUser()">Create</button>
      </form>
    </div>
    <div class="create-user-form">
      <h2>Create Admin</h2>
      <form>
        <label for="username">
          <input type="text" name="username" id="username" v-model="createAdmin.username" required placeholder="username">
        </label>
        <label for="password">
          <input type="password" name="password" id="password" v-model="createAdmin.password" required placeholder="password">
        </label>
        <button @click.prevent="handleCreateAdmin()">Create</button>
      </form>
    </div>
    <div class="user-list">
      <h2>Active Users</h2>
      <button @click.prevent="refreshPending(true)">refresh</button>
      <table>
        <tr>
          <th>id</th>
          <th>username</th>
          <th>role</th>
          <th></th>
        </tr>
        <tr v-for="user of allUsers" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.role }}</td>
          <td><button class="deleteUserButton" @click.prevent="handleDeleteUser(user.id)">Delete</button></td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import BackendService from '@/service/BackendService';
/* eslint-disable max-len */

export default {
  data() {
    return {
      createUsers: '',
      createUsersResponse: {
        createUsersSuccess: [],
        createUsersError: [],
      },
      createUser: {
        username: '',
        password: '',
      },
      createAdmin: {
        username: '',
        password: '',
      },
    };
  },

  moutned() {
    this.refreshPending();
  },

  methods: {
    async handleCreateUsers() {
      let usersToCreateArr = this.createUsers.split(',');
      // eslint-disable-next-line no-restricted-syntax
      for (const username of usersToCreateArr) {
        usersToCreateArr[usersToCreateArr.indexOf(username)] = username.trim().toLowerCase(); // remove whitespaces and newlines
      }
      // eslint-disable-next-line no-restricted-syntax
      usersToCreateArr = usersToCreateArr.filter((el) => el != null && el !== undefined && el !== ''); // removes empty names which can come from asdf,,asdf (double comma)
      usersToCreateArr = [...new Set(usersToCreateArr)]; // removes duplicates

      // make api call
      await BackendService.createUsers(usersToCreateArr, this.username, this.password)
        .then((response) => {
          // handle error users
          this.createUsersResponse.createUsersError = [];
          // eslint-disable-next-line no-restricted-syntax
          for (const [erroruser, errortype] of Object.entries(response.data.errorUsers)) {
            // eslint-disable-next-line prefer-const
            let user = JSON.parse(erroruser);
            // eslint-disable-next-line dot-notation
            user['errorType'] = errortype;
            this.createUsersResponse.createUsersError.push(user);
          }

          // handle created users
          this.$store.commit('setAllUsers', []);
          this.createUsersResponse.createUsersSuccess = [];
          // eslint-disable-next-line no-restricted-syntax
          for (const createdUser of response.data.createdUsers) {
            this.createUsersResponse.createUsersSuccess.push(createdUser);
          }
          this.$store.commit('pushNotification', { head: 'UsersCreated', text: 'see tables and COPY PASSWORDS!!!', type: 'good' });
          this.refreshPending();
        });
    },
    async handleCreateAdmin() {
      BackendService.createAdmin(this.createAdmin.username, this.createAdmin.password, this.username, this.password)
        .then((response) => {
          if (response.status === 400) {
            this.$store.commit('pushNotification', { head: 'Error Creating User', text: 'Username maybe alredy exist', type: 'bad' });
          } else {
            this.refreshPending();
            this.$store.commit('pushNotification', { head: 'Admin created', text: `username: ${this.createAdmin.username} password: ${'*'.repeat(this.createAdmin.password.length)}`, type: 'good' });
          }
        })
        .catch(() => {
          this.$store.commit('pushNotification', { head: 'Error Creating User', text: 'Username maybe alredy exist', type: 'bad' });
        });
    },
    async handleCreateUser() {
      BackendService.createUser(this.createUser.username, this.createUser.password, this.username, this.password)
        .then((response) => {
          if (response.status === 400) {
            this.$store.commit('pushNotification', { head: 'Error Creating User', text: 'Username maybe alredy exist', type: 'bad' });
          } else {
            this.refreshPending();
            this.$store.commit('pushNotification', { head: 'User created', text: `username: ${this.createUser.username} password: ${'*'.repeat(this.createUser.password.length)}`, type: 'good' });
          }
        })
        .catch(() => {
          this.$store.commit('pushNotification', { head: 'Error Creating User', text: 'Username maybe alredy exist', type: 'bad' });
        });
    },
    sortById(a, b) {
      if (a.id > b.id) {
        return -1;
      }
      return 1;
    },
    async refreshPending(calledByButton = false) {
      await BackendService.getAllUsers(this.username, this.password)
        .then((response) => {
          const sorted = response.data.sort(this.sortById); // newest users on top
          if (this.allUsers.length === response.data.length && calledByButton) {
            this.$store.commit('pushNotification', { head: 'Already up to date', text: '', type: 'good' });
          }
          this.$store.commit('setAllUsers', sorted);
        });
    },
    async handleDeleteUser(id) {
      BackendService.deleteUser(id, this.username, this.password)
        .then((response) => {
          if (response.status === 200) {
            console.log('delete success');
            this.$store.commit('pushNotification', { head: 'Deleted Successful', text: '', type: 'good' });
          } else {
            this.$store.commit('pushNotification', { head: 'Error', text: `Couldn't delete user with id${id}`, type: 'bad' });
          }
          this.refreshPending();
        });
    },
  },

  computed: {
    allUsers() {
      return this.$store.state.admin.allUsers;
    },
    username() {
      return this.$store.state.userdata.username;
    },
    password() {
      return this.$store.state.userdata.password;
    },
  },
};

</script>

<style lang="scss" scoped>
.container {
  height: fit-content;
  width: 50%;
  min-width: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  > * {
    padding: 16px;
    input, button {
      margin: 16px 4px;
    }
    input {
      padding: 4px;
    }
  }
}
th {
  font-weight: bolder;
  font-size: 16px;
}
.error-users{
  tr {
    > td {
      border-top: 4px solid red;
    }
  }
  td,th {
    padding-right: 4px;
    border-right: 4px solid red;
    &:last-child {
      padding-right: 0;
      border-right: none;
    }
  }
}
.success-users{
  tr {
    > td {
      border-top: 4px solid greenyellow;
    }
  }
  td,th {
    padding-right: 4px;
    border-right: 4px solid greenyellow;
    &:last-child {
      padding-right: 0;
      border-right: none;
    }
  }
}
.user-list {
  tr {
    > td {
      border-top: 4px solid lightblue;
    }
  }
  td,th {
    padding: 4px 16px;
    border-right: 4px solid lightblue;
    &:last-child {
      border-right: none;
    }
  }
  .deleteUserButton {
    background-color: red;
    border: none;
    border-radius: 8px;
    padding: 8px 16px;
    &:hover {
      background-color: orange;
      cursor: pointer;
    }
  }
}
</style>
