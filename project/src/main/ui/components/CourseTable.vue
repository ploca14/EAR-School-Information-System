<template>
  <div>
    <template v-if="$fetchState.pending">
      <v-skeleton-loader type="table-thead"/>
      <v-skeleton-loader type="table-tbody"/>
      <v-skeleton-loader type="table-tfoot"/>
    </template>
    <p v-else-if="$fetchState.error">
      {{ $fetchState.error.response.data.status }} {{ $fetchState.error.response.data.error }}
      {{ $fetchState.error.response.data.message }}
    </p>
    <template v-else>
      <v-data-table
        :headers="headers"
        :items="courses"
        :items-per-page="5"
        class="elevation-1"
      >
        <template v-slot:item.completionType="{ item }">
          <v-chip dark small>
            {{ getAbbreviation(item.completionType) }}
          </v-chip>
        </template>
        <template v-slot:item.enroll="{ item }">
          <v-btn color="primary" @click="enrollCourse(item)">
            Enroll
          </v-btn>
        </template>
      </v-data-table>
      <v-snackbar
        :value="snackbar"
        :color="colour"
        :timeout="2000"
      >
        {{ message }}
      </v-snackbar>
    </template>
  </div>
</template>

<script>
export default {
  props: {
    semester: { type: String, required: true },
  },

  data: () => ({
    courses: [],
    headers: [
      {
        text: 'Code',
        value: 'code',
      },
      {
        text: 'Name',
        value: 'name',
      },
      {
        text: 'Credits',
        value: 'credits',
      },
      {
        text: 'Completion',
        value: 'completionType',
        align: 'center',
      },
      {
        text: 'Enroll',
        value: 'enroll',
        align: 'center',
      },
    ],
    snackbar: false,
    message: '',
    colour: '',
  }),

  async fetch() {
    let courses = await this.$axios.$get(`/semester/${this.semester}/courses`);
    this.courses = courses;
  },

  methods: {
    getAbbreviation(completionType) {
      const map = {
        'CLFD_CREDIT' : "GA",
        'CREDIT' : "A",
        'EXAM' : "EX",
        'NOTHING' : "N",
        'DEFENCE' : "D",
        'CREDIT_EXAM' : "A, EX",
      }

      return map[completionType];
    },

    async enrollCourse({ code }) {
      this.snackbar = false;
      let payload = {
        course: code,
        semester: this.semester,
      };
      await this.$axios.$post('/my/courses', payload)
      .then(() => {
        this.colour = 'success';
        this.message = 'User has been successfully enrolled';
        this.snackbar = true;
      })
      .catch((error) => {
        this.colour = 'error';
        this.message = error.response.data.message ? error.response.data.message : error.response.data.error;
        this.snackbar = true;
      });
    }
  },

  watch: {
    semester: () => {
      this.$fetch();
    }
  }
}
</script>
