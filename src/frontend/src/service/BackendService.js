/* eslint-disable class-methods-use-this */

import axios from 'axios';

const BASE_URL = 'http://localhost:6900';

class BackendService {
  makeBasicAuth(username, password) {
    return `Basic ${btoa(`${username}:${password}`)}`;
  }

  getUserInfo(username, password) {
    return axios.get(`${BASE_URL}/api/client/getUserInfo`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  createUsers(array, username, password) {
    console.log(username + password);
    console.log(array);
    return axios.post(
      `${BASE_URL}/api/admin/createUsers`,
      {
        userCount: 0, // not important
        usernames: array,
      },
      {
        headers: {
          Authorization: this.makeBasicAuth(username, password),
        },
      },
    );
  }

  getAllUsers(username, password) {
    return axios.get(`${BASE_URL}/api/admin/getUsers`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  deleteUser(id, username, password) {
    return axios.get(`${BASE_URL}/api/admin/delete?id=${id}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  createAdmin(newUsername, newPassword, username, password) {
    return axios.post(`${BASE_URL}/api/admin/createAdmin`, {
      username: newUsername,
      password: newPassword,
    }, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  createUser(newUsername, newPassword, username, password) {
    return axios.post(`${BASE_URL}/api/admin/createUser`, {
      username: newUsername,
      password: newPassword,
    }, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }
}

export default new BackendService();
