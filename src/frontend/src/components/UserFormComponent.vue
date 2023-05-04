<!-- eslint-disable max-len -->
<template>
  <div class="container">
    <h2>Alle eingaben muessen nach der folgenden Form sein:<br>vorname.nachname</h2>
    <form class="form-bili">
      <label for="bili">Bili
        <select name="bili" id="bili" v-model="bili">
          <option value="true">ja</option>
          <option value="false">nein</option>
        </select>
        <button type="submit" @click.prevent="setBili()">Commit</button>
      </label>
    </form>
    {{ (bili == 'true') ? 'Bili ist gewaehlt' : 'Bili ist nicht gewaehlt' }}
    <form class="form-wp">
      <label for="wp">Wahlpflicht (leer: klartexter)
        <select name="wp" id="wp" v-model="wp">
          <option value="mint">MINT</option>
          <option value="klartexter">Klartexter</option>
        </select>
        <button type="submit" @click.prevent="setWP()">Commit</button>
      </label>
    </form>
    {{ (wp === '') ? '' : 'Wahlpflicht: ' }} {{ wp }}
    <form class="form-priority">
      <label for="prio">Garantierter Mitschueler
        <input type="text" name="prio" id="prio" placeholder="Garantierter Mitschueler" v-model="prio" required>
      </label>
      <button type="submit" @click.prevent="setPrio()">Commit</button>
    </form>
    <form class="form-five">
      <label for="five-input">Fuenf weitere Wunschmitschueler (sortiert nach prioritaet)
        <input type="text" name="five-input" id="five-input" placeholder="1" v-model="five1">
        <input type="text" name="five-input" id="five-input" placeholder="2" v-model="five2">
        <input type="text" name="five-input" id="five-input" placeholder="3" v-model="five3">
        <input type="text" name="five-input" id="five-input" placeholder="4" v-model="five4">
        <input type="text" name="five-input" id="five-input" placeholder="5" v-model="five5">
      </label>
      <button class="hidden" type="submit" @click.prevent="setFive()">Commit</button>
    </form>
    <div class="display-five">
      <p v-for="user of parseFive()" :key="user">{{ user }}</p>
    </div>
    <button type="submit" @click.prevent="setFive()">Commit</button>
    <form class="form-black">
      <label for="blacklist">Blacklist (getrennt mit ',')
        <input type="text" name="blacklist" id="blacklist" v-model="blacklist">
      </label>
      <button type="hidden" @click.prevent="setBlacklist()" style="display: none;"></button> <!-- this triggers the setBlacklist function on enter in input -->
    </form>
    <div class="display-blacklist">
      <p v-for="user of parseBlacklist(blacklist)" :key="user">{{ user }}</p>
    </div>
    <button type="submit" @click.prevent="setBlacklist()">Commit</button>
    <div class="display-user">
      <h2 class="review">Nochmal drueber schauen</h2>
      <p class="bili">Bili [ja/nein]: {{ (userBili) ? 'ja' : 'nein' }}</p>
      <p class="wp">Wahlpflicht: {{ userWP }}</p>
      <p class="prio">Wunschpartner: {{ userPrio }}</p>
      <br>
      <ul class="five">
        weitere Wunschpartner:
        <li v-for="user of fiveList" :key="user">{{ user }}</li>
      </ul>
      <br>
      <ul>
        Blacklist:
        <li v-for="user of userBlacklist" :key="user">{{ user }}</li>
      </ul>
    </div>
    <button @click.prevent="done()">Bestaetigen</button>
  </div>
</template>

<script>
/* eslint-disable no-restricted-syntax */
/* eslint-disable no-await-in-loop */
import BackendService from '@/service/BackendService';

export default {
  name: 'FrontendUserFormComponent',

  data() {
    return {
      bili: false,
      userWP: '',
      userBili: '',
      userBlacklist: [],
      fiveList: [],
      userPrio: '',
      wp: '',
      prio: '',
      five1: '',
      five2: '',
      five3: '',
      five4: '',
      five5: '',
      blacklist: '',
    };
  },

  mounted() {
    this.fetchUserData();
  },

  methods: {
    async done() {
      await BackendService.done(this.username, this.password);
      this.$store.commit('pushNotification', { head: 'Fertig!', text: 'Es sind jetzt keine weiteren eingaben moeglich', type: 'good' });
    },
    async fetchUserData() {
      await BackendService.getUserInfo(this.username, this.password)
        .then((response) => {
          this.userBili = response.data.bilingual;
          this.userPrio = response.data.priorityMate;
          this.userWP = response.data.wp;
          this.userBlacklist = response.data.blacklistedMates;
          this.fiveList = Object.values(response.data.fiveMatesPriorityName);
        });
    },
    parseFive() {
      return [
        this.five1.toLowerCase().trim(),
        this.five2.toLowerCase().trim(),
        this.five3.toLowerCase().trim(),
        this.five4.toLowerCase().trim(),
        this.five5.toLowerCase().trim(),
      ];
    },
    parseBlacklist(blacklist) {
      const retArray = [];
      const tmpArr = blacklist.split(',');
      for (const username of tmpArr) {
        retArray.push(username.toLowerCase().trim());
      }
      return [...new Set(retArray.filter((el) => el != null && el !== undefined && el !== ''))];
    },
    async setPrio() {
      await BackendService.checkUserExistance(this.username, this.password, this.prio)
        .then(async (response) => {
          if (response.data) {
            await BackendService.setPrio(this.username, this.password, /* string */ this.prio)
              .then((response2) => {
                if (response2.data.includes('NEW PRIO MATE NOT SET, CAUSE IS CONTAINED IN BLACKLIST')) {
                  this.$store.commit('pushNotification', { head: 'ACHTUNG! Dieser Schueler befindet sich auf deiner Blacklist', text: 'Bitte suche einen anderen Schueler aus, oder mache eine neue Blacklist, der schueler wurde nicht gesetzt', type: 'bad' });
                }
              });
          } else if (!response.data) {
            this.$store.commit('pushNotification', { head: 'Der Nutzer Existiert nicht', text: `Der Nutzer ${this.prio} konnte nicht gefunden werden`, type: 'bad' });
          } else {
            this.$store.commit('pushNotification', { head: 'Error request', text: 'Bitte frage einen Admin, [the request setPrio() did not succeed]', type: 'bad' });
          }
        });
      this.fetchUserData();
    },
    async setBlacklist() {
      const blacklist = this.parseBlacklist(this.blacklist);
      const existingUsers = [];
      await BackendService.resetBlacklist(this.username, this.password);
      for (const username of blacklist) {
        let userExists;
        await BackendService.checkUserExistance(this.username, this.password, username)
          .then((response) => {
            if (response.data) {
              userExists = true;
            } else {
              this.$store.commit('pushNotification', { head: 'ERROR_USER_DOES_NOT_EXIST', text: `the user "${username.toUpperCase()}" does not exist`, type: 'bad' });
            }
            if (userExists) {
              existingUsers.push(username);
            }
          });
      }
      for (const username of existingUsers) {
        await BackendService.addToBlacklist(this.username, this.password, username)
          .then((response) => {
            if (response.data.includes('NOT SET, CAUSE USER IN FIVE MATES')) {
              this.$store.commit('pushNotification', { head: 'Nutzer in Wunschliste', text: `nutzer ${username.toUpperCase()} nicht gesetzt, er befindet sich in der wunschliste`, type: 'bad' });
            }
            if (response.data.includes('NOT SET, CAUSE USER IS PRIO USER')) {
              this.$store.commit('pushNotification', { head: 'Nutzer in garantierter Wunschpartner', text: `nutzer ${username.toUpperCase()} nicht gesetzt, er ist der garantierte partner`, type: 'bad' });
            }
          });
      }
      this.fetchUserData();
    },
    async setFive() {
      const users = this.parseFive();
      const existingUsers = [];
      for (const username of users) {
        let userExists;
        await BackendService.checkUserExistance(this.username, this.password, username)
          .then((response) => {
            if (response.data) {
              userExists = true;
            } else {
              this.$store.commit('pushNotification', { head: 'ERROR_USER_DOES_NOT_EXIST', text: `the user "${username.toUpperCase()}" does not exist`, type: 'bad' });
            }
            if (userExists) {
              existingUsers.push(username);
            }
          });
      }
      await BackendService.setFive(this.username, this.password, users)
        .then((response) => {
          if (response.data.includes('BUT THEY CONTAIN THE PRIORITY MATE')) {
            this.$store.commit('pushNotification', { head: 'Garantierter Partner in der Liste', text: 'Dein garantierter Partner befindet sich in der Liste, das ist ok, aber bist du dir sicher, dass du diesen Wunsch verschwenden willst?', type: 'bad' });
          } else if (response.data.includes('BUT THEY CONTAIN DUPLICATES')) {
            this.$store.commit('pushNotification', { head: 'Duplikat in der Liste', text: 'Deine Wunschliste wurde gesetzt, aber sie enthaelt Duplikate, willst du diesen Wunsch verschwenden?', type: 'bad' });
          } else if (response.data.includes('NOT SET, ONE USER FROM BLACKLIST')) {
            this.$store.commit('pushNotification', { head: 'Blacklist ueberschneidet sich', text: 'Ein nutzer ist in der Blacklist und in der Wunschliste, bitte behebe das, die Wuensche wurden nicht gesetzt.', type: 'bad' });
          }
        });
      this.fetchUserData();
    },
    async setWP() {
      if (this.wp === 'mint') this.wp = 'MINT'; else this.wp = 'KLAR';
      await BackendService.setWP(this.username, this.password, /* string */ this.wp);
      this.fetchUserData();
    },
    async setBili(counter = 0) {
      const biliBefore = this.bili;
      if (this.bili === 'true') { // v-model stores in string form
        this.bili = true;
      } else {
        this.bili = false;
      }
      await BackendService.setBili(this.username, this.password, /* boolean */ this.bili);
      this.bili = biliBefore; // restore string value
      if (counter < 5) {
        await this.setBili(counter + 1); /* till now the best soloution,
        since the bili select is a bit buggy. It's cause of the string to bool convertions */
      }
      this.fetchUserData();
    },
  },

  computed: {
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
  display: flex;
  flex-direction: column;
  margin-bottom: 100px !important; /* don't quite no why !important
  is to make a bit of headroom for scroll */
  * {
    margin: 4px;
    height: fit-content;
  }
  form {
    display: flex;
    flex-direction: column;
    width: fit-content;
    min-width: 300px;
  }
}
.review {
  color: green;
}
.hidden {
  display: none;
}
</style>
