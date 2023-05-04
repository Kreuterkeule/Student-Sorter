<!-- eslint-disable max-len -->
<template>
  <div class="container">
    <div class="create-users-form">
      <button @click.prevent="generateClasses()">Klassen Generieren</button>
      <div class="class-results">
        <table>
          <tr>
            <!-- name ?bili ?mint... -->
          </tr>
          <tr>
            <!-- schueler liste -->
          </tr>
        </table>
      </div>
      <button @click.prevent="refreshPending()">refresh</button>
      <div class="review">
        {{ computeLeft }} Leute noch nicht abgestimmt:
        <ul>
          <li v-for="user of left" :key="user.username">{{ user.username }}</li>
        </ul>
      </div>
      <h2>Create Users</h2> <!-- maybe check user existance before committing needing a rest endpoint for that here -->
      <div class="admin-form">
        <form>
          <label for="usernames">Usernames (seperated by ',' need to be unique f.e 'username.password' passwords are generated automatically and need to be copied afterwards since just their hashes are stored for security reasons!)
            <input type="text" name="usernames" id="usernames" placeholder="usernames" required v-model="createUsers">
          </label>
          <button type="submit" @click.prevent="handleCreateUsers()">Create</button>
        </form>
      </div>
      <div class="success-users" v-if="createUsersResponse.createUsersSuccess.length > 0">
        <h2 class="green">Success creating: </h2> <!-- show if list not empty -->
        <table id="successUsersTable">
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
        <button @click.prevent="downloadCsv('#successUsersTable')">Export CSV</button>
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
          <th>enabled?</th>
          <th></th>
          <th></th>
          <th></th>
        </tr>
        <tr v-for="user of allUsers" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.role }}</td>
          <td style="color: green;" v-if="user.enabled">ja</td>
          <td style="color: red;" v-else>nein</td>
          <td><button class="deleteUserButton" @click.prevent="handleDeleteUser(user.id)">Delete</button></td>
          <td><button class="enableButton" @click.prevent="enableUser(user.username)">Enable</button></td>
          <td><button class="disableButton" @click.prevent="disableUser(user.username)">Disable</button></td>
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

  async moutned() {
    await this.refreshPending();
    await this.refreshPending();
  },

  methods: {
    async generateClasses() {
      console.log('genrating');
    },
    downloadCsv(id, separator = ',') {
      // thanks to Calumah@StackOverflow (https://stackoverflow.com/questions/15547198/export-html-table-to-csv-using-vanilla-javascript)
      // Select rows from table_id
      const rows = document.querySelectorAll(`${id} tr`);
      // Construct csv
      const csv = [];
      for (let i = 0; i < rows.length; i += 1) {
        const row = [];
        const cols = rows[i].querySelectorAll('td, th');
        for (let j = 0; j < cols.length; j += 1) {
          // Clean innertext to remove multiple spaces and jumpline (break csv)
          let data = cols[j].innerText.replace(/(\r\n|\n|\r)/gm, '').replace(/(\s\s)/gm, ' ');
          // Escape double-quote with double-double-quote (see https://stackoverflow.com/questions/17808511/properly-escape-a-double-quote-in-csv)
          data = data.replace(/"/g, '""');
          // Push escaped string
          row.push(`"${data}"`);
        }
        csv.push(row.join(separator));
      }
      const csvString = csv.join('\n');
      // Download it
      const filename = `export_createdUsers_${new Date().toLocaleDateString()}.csv`;
      const link = document.createElement('a');
      link.style.display = 'none';
      link.setAttribute('target', '_blank');
      link.setAttribute('href', `data:text/csv;charset=utf-8,${encodeURIComponent(csvString)}`);
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
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
          this.$store.commit('pushNotification', { head: 'Users Created', text: 'Unbedingt die passwoerter kopieren oder als csv exportieren, sonst sie sind verloren', type: 'good' });
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
            this.$store.commit('pushNotification', { head: 'Deleted Successful', text: '', type: 'good' });
          } else {
            this.$store.commit('pushNotification', { head: 'Error', text: `Couldn't delete user with id${id}`, type: 'bad' });
          }
          this.refreshPending();
        });
    },
    async enableUser(username) {
      await BackendService.enable(username, this.username, this.password);
      this.refreshPending();
    },
    async disableUser(username) {
      await BackendService.disable(username, this.username, this.password);
      this.refreshPending();
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
    computeLeft() {
      return this.left.length;
    },
    left() {
      const left = [];
      /* eslint-disable-next-line */
      for (const user of this.allUsers) {
        if (user.enabled && user.role === 'USER') {
          left.push(user);
        }
      }
      return left;
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
      background-color: rosybrown;
      cursor: pointer;
    }
  }
  .enableButton {
    background-color: green;
    border: none;
    border-radius: 8px;
    padding: 8px 16px;
    &:hover {
      background-color: lightgreen;
      cursor: pointer;
    }
  }
  .disableButton {
    background-color: darkorange;
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
