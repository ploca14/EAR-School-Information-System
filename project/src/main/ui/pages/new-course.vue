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
                v-model="payload.name"
                :rules="rules.required"
                label="Name"
                required
              ></v-text-field>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model="payload.code"
                    label="Code"
                    :rules="rules.required"
                    required
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="payload.credits"
                    label="Credits"
                    :rules="rules.required"
                    type="number"
                    min="0"
                    required
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-textarea
                v-model="payload.description"
                label="Description"
              ></v-textarea>
              <v-select
                v-model="payload.completionType"
                :items="completionTypes"
                label="Completion type"
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
  middleware: 'isstudydep',

  data() {
    return {
      title: 'Create new course',
      valid: false,
      payload: {
        name: '',
        credits: 0,
        description: '',
        code: '',
        completionType: 'UNDEFINED',
      },
      completionTypes: [
        {
          text: 'Klasifikovaný zápočet',
          value: 'CLFD_CREDIT',
        },
        {
          text: 'Zápočet',
          value: 'CREDIT',
        },
        {
          text: 'Zkouška',
          value: 'EXAM',
        },
        {
          text: 'Nic',
          value: 'NOTHING',
        },
        {
          text: 'Obhajoba',
          value: 'DEFENCE',
        },
        {
          text: 'Nedefinovaný',
          value: 'UNDEFINED',
        },
        {
          text: 'Zápočet a zkouška',
          value: 'CREDIT_EXAM',
        },
      ],
      rules: {
        required: [v => !!v || 'This field is required'],
      },
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
      await this.$axios.post('/courses', this.payload)
        .then(() => {
          this.colour = 'success';
          this.message = 'Course has been successfully created';
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
