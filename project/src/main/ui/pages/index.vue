<template>
  <v-row justify="center" align="center">
    <v-col cols="12" sm="8" md="6">
      <v-card>
        <v-card-title>
          User
        </v-card-title>
        <v-card-text>
          <span class="text-subtitle-1">{{ fullName }}</span> ({{ loggedInUser.username }})

        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      title: 'Home'
    }
  },

  head() {
    return {
      title: this.title,
    }
  },

  computed: {
    ...mapGetters(['isAuthenticated', 'loggedInUser']),
    fullName() {
      return `${this.loggedInUser.firstName} ${this.loggedInUser.lastName}`
    },
  }
}
</script>
