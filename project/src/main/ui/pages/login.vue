<template>
  <div class="fill-height d-flex justify-center align-center">
    <v-card width="400">
      <v-card-title class="justify-center">
        Login
      </v-card-title>
      <v-card-text>
        <v-form v-model="valid" @submit.prevent="userLogin">
          <v-container>
            <v-text-field
              v-model="payload.username"
              :rules="usernameRules"
              :counter="10"
              label="Username"
              required
            ></v-text-field>
            <v-text-field
              v-model="payload.password"
              :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
              :rules="passwordRules"
              :type="show ? 'text' : 'password'"
              name="input-10-1"
              label="Password"
              hint="At least 6 characters"
              counter
              @click:append="show = !show"
            ></v-text-field>

            <div class="d-flex justify-center">
              <v-btn
                :disabled="!valid"
                color="success"
                type="submit"
              >
                Log-in
              </v-btn>
            </div>
          </v-container>
        </v-form>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  layout: 'blank',

  data() {
    return {
      valid: false,
      payload: {
        username: '',
        password: '',
      },
      usernameRules: [
        v => !!v || 'Username is required',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => v.length >= 6 || 'Name must be at least 6 characters',
      ],
      show: false,
    }
  },

  methods: {
    async userLogin() {
      try {
        await this.$auth.loginWith('local', { data: this.payload });
      } catch (err) {
        console.log(err)
      }
    }
  }
}
</script>
