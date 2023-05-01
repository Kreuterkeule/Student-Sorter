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
}

export default new BackendService();
