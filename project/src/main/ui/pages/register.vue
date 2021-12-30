<template>
  <v-row justify="center" align="center">
    <v-col cols="12" sm="8" md="6">
      <v-card>
        <v-card-title class="justify-center">
          {{ title }}
        </v-card-title>
        <v-card-text>
          <v-form
            ref="form"
            v-model="valid"
            @submit.prevent="submit()"
          >
            <v-container>
              <v-text-field
                v-model="payload.username"
                :rules="rules.required"
                label="Username"
                required
              ></v-text-field>
              <v-text-field
                v-model="payload.firstName"
                label="First name"
                :rules="rules.required"
                required
              ></v-text-field>
              <v-text-field
                v-model="payload.lastName"
                label="Last name"
                :rules="rules.required"
                required
              ></v-text-field>
              <v-text-field
                v-model="payload.email"
                type="email"
                label="E-mail"
                :rules="rules.required"
                required
              ></v-text-field>
              <v-text-field
                v-model="payload.password"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                :rules="rules.password"
                :type="showPassword ? 'text' : 'password'"
                name="input-10-1"
                label="Password"
                hint="At least 6 characters"
                counter
                @click:append="showPassword = !showPassword"
              ></v-text-field>
              <v-select
                v-model="payload.role"
                :items="roles"
                label="Role"
              ></v-select>

              <div class="d-flex justify-center">
                <v-btn
                  :disabled="!valid"
                  color="success"
                  type="submit"
                >
                  Create
                </v-btn>
              </div>
            </v-container>
          </v-form>

          <v-snackbar
            :value="message"
            :color="colour"
          >
            {{ message }}
          </v-snackbar>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  middleware: 'authenticated',

  data() {
    return {
      title: 'Create new user',
      valid: false,
      payload: {
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        role: '',
        password: '',
      },
      roles: [
        {
          text: 'Admin',
          value: 'ADMIN',
        },
        {
          text: 'Regular user',
          value: 'REGULAR_USER',
        },
        {
          text: 'Study department employee',
          value: 'STUDY_DEPARTMENT_EMPLOYEE',
        },
      ],
      rules: {
        required: [v => !!v || 'This field is required'],
        password: [
          v => !!v || 'Password is required',
          v => v.length >= 6 || 'Name must be at least 6 characters',
        ],
      },
      showPassword: false,
      message: '',
      colour: '',
    }
  },

  head() {
    return {
      title: this.title,
    }
  },

  methods: {
    async submit() {
      await this.$axios.post('/users', this.payload)
        .then(() => {
          this.colour = 'success';
          this.message = 'User has been successfully created';
          this.$refs.form.reset();
        })
        .catch(() => {
          this.colour = 'error';
          this.message = 'Something went wrong';
        })
    }
  }
}
</script>
