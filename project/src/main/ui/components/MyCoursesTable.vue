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
        <template v-slot:item.unenroll="{ item }">
          <v-btn color="red" @click="unenrollCourse(item)">
            Unenroll
          </v-btn>
        </template>
      </v-data-table>
      <v-snackbar
        v-model="snackbar"
        :timeout="2000"
        :color="colour"
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
        text: 'Semester',
        value: 'semester',
      },
      {
        text: 'Unenroll',
        value: 'unenroll',
        align: 'center',
      },
    ],
    snackbar: false,
    message: '',
    colour: '',
  }),

  async fetch() {
    this.snackbar = false;
    let courses = await this.$axios.$get(`/my/courses/${this.semester}`);
    this.courses = courses.map(({course, semester}) => ({
      ...course,
      semester: semester.code,
    }));
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

    async unenrollCourse(course) {
      this.snackbar = false;
      await this.$axios.delete(`/my/courses/${course.code}/${course.semester}`)
      .then(() => {
        this.colour = 'success';
        this.message = 'User has been successfully unenrolled';
        this.snackbar = true;
        this.courses = this.courses.filter(c => c !== course);
      })
      .catch((error) => {
        this.colour = 'error';
        this.message = error.response.data.message ? error.response.data.message : error.response.data.error;
        this.snackbar = true;
      });
    }
  },

  watch: {
    semester() {
      this.$fetch();
    },
  }
}
</script>
