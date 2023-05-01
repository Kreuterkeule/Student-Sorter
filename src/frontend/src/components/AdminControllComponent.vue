<!-- eslint-disable max-len -->
<template>
  <div class="container">
    <div class="create-users-form">
      <h2>Create Users</h2> <!-- maybe check user existance before committing needing a rest endpoint for that here -->
      <div class="admin-form">
        <label for="usernames">Usernames (seperated by ',' need to be unique f.e 'regine.teschke' passwords are generated automatically and need to be copied afterwards since just their hashes are stored for security reasons!)
          <input type="text" name="usernames" id="usernames" placeholder="usernames" required>
        </label>
        <button type="submit" @click="handleCreateUsers()">Create</button>
      </div>
      <div class="success-users">
        <h2 class="green">Success creating: </h2> <!-- show if list not empty -->
        id | username | password
      </div>
      <div class="error-users"> <!-- check if list empty -->
        <h2 class="orange">Errors creating: </h2>
        error-type | username | password
      </div>
    </div>
    <div class="create-admin-form">
      <h2>Create Admin</h2>
    </div>
    <div class="create-user-form">
      <h2>Create Single User</h2>
    </div>
    <div class="user-list">
      <h2>Pending Users</h2>
      <!-- id | username | role | ?maybe prefered mates and stuff | delete button -->
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {

    };
  },

  methods: {

  },

  mounted() {

  },
};

</script>

<style lang="scss" scoped>
.container {
  overflow: scroll;
  height: 80vh;
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
</style>
