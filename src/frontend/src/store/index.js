/* eslint-disable import/no-extraneous-dependencies */
import Vue from 'vue';
import Vuex from 'vuex';
import createPersistState from 'vuex-plugin-persistedstate';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    loggedIn: false,
    userdata: {
      isAdmin: false,
      username: '',
      password: '',
    },
    notifyMessages: [],
    admin: {
      allUsers: [],
    },
  },
  getters: {
  },
  mutations: {
    setLoggedIn(state, bool) {
      state.loggedIn = bool;
    },
    reset(state) {
      state.loggedIn = false;
      state.userdata.username = '';
      state.userdata.isAdmin = false;
      state.userdata.password = '';
      state.admin.allUsers = [];
    },
    setAdmin(state, bool) {
      state.userdata.isAdmin = bool;
    },
    setUsername(state, username) {
      state.userdata.username = username;
    },
    setPassword(state, password) {
      state.userdata.password = password;
    },
    setAllUsers(state, allUsers) {
      state.admin.allUsers = allUsers;
    },
    resetNotifyMessages(state) {
      state.notifyMessages = [];
    },
    pushNotification(state, message) {
      state.notifyMessages.push(message);
    },
    setNotifications(state, emptyArray) {
      state.notifyMessages = emptyArray;
    },
  },
  actions: {
  },
  modules: {
  },
  plugins: [createPersistState()],
});
