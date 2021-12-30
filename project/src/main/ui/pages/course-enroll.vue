<template>
  <v-row justify="center">
    <v-col xl="8">
      <h2 class="mb-5 mt-3">{{ title }}</h2>
      <v-select
        v-model="semester"
        :items="semesters"
        label="Semester"
      ></v-select>
      <template v-if="semester">
        <course-table :semester="semester"/>
      </template>
      <template v-else>
        No semester selected
      </template>
    </v-col>
  </v-row>
</template>

<script>
export default {
  data() {
    return {
      semester: '',
      title: 'Enroll course',
    };
  },

  head() {
    return {
      title: this.title,
    }
  },

  async asyncData({ $axios }) {
    const capitalize = (string) => {
      return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

    const filter = ({state}) => state === 'PREPARATION';

    const convert = ({code, type, year}) => {
      return {
        text: `${code} - ${capitalize(type)} ${year}`,
        value: code,
      };
    };

    const semesters = await $axios.$get('/semester');
    const options = semesters.filter(filter).map(convert);

    return {
      semesters: options,
    }
  },
}
</script>
