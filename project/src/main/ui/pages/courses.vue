<template>
  <v-container>
    <v-select
      v-model="semester"
      :items="semesters"
      label="Semester"
    ></v-select>
    <template v-if="semester">
      <my-courses-table :semester="semester"/>
    </template>
    <template v-else>
      No semester selected
    </template>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      semester: '',
    };
  },

  async asyncData({ $axios }) {
    const capitalize = (string) => {
      return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

    const convert = ({code, type, year}) => {
      return {
        text: `${code} - ${capitalize(type)} ${year}`,
        value: code,
      };
    };

    const semesters = await $axios.$get('/semester');
    const options = semesters.map(convert);

    return {
      semesters: options,
    }
  },
}
</script>
