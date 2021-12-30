<template>
  <v-row justify="center">
    <v-col xl="8">
      <h2 class="mb-5 mt-3">{{ title }}</h2>
      <timetable />
    </v-col>
  </v-row>
</template>

<script>
export default {
  data() {
    return {
      title: 'Timetable'
    }
  },

  head() {
    return {
      title: this.title,
    }
  },
}
</script>
