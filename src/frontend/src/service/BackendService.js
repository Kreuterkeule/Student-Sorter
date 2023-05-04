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

  enable(toEnalbe, username, password) {
    return axios.get(`${BASE_URL}/api/admin/enable?username=${toEnalbe}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  disable(toDisable, username, password) {
    return axios.get(`${BASE_URL}/api/admin/disable?username=${toDisable}`, {
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

  setBili(username, password, bili) {
    let state = '';
    if (bili) {
      state = 'BILI';
    } else {
      state = 'NOT_BILI';
    }
    axios.get(`${BASE_URL}/api/client/changeBilingual?state=${state}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  checkUserExistance(username, password, prio) {
    return axios.get(`${BASE_URL}/api/client/checkUser?username=${prio}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  setPrio(username, password, prio) {
    return axios.get(`${BASE_URL}/api/client/changePriorityMate?username=${prio}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  setWP(username, password, wp) {
    return axios.get(`${BASE_URL}/api/client/setWp?state=${wp}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  addToBlacklist(username, password, blackUser) {
    return axios.get(`${BASE_URL}/api/client/addToBlacklist?username=${blackUser}`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  resetBlacklist(username, password) {
    return axios.get(`${BASE_URL}/api/client/resetBlacklist`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  setFive(username, password, array) {
    const obj = {};
    for (let i = 0; i < array.length; i += 1) {
      obj[`name${i + 1}`] = array[i];
    }
    return axios.post(`${BASE_URL}/api/client/changeFiveMates`, obj, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }

  done(username, password) {
    return axios.get(`${BASE_URL}/api/client/done`, {
      headers: {
        Authorization: this.makeBasicAuth(username, password),
      },
    });
  }
}

export default new BackendService();
