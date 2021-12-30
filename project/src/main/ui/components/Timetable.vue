<template>
  <div>
    <template v-if="$fetchState.pending">
      <v-skeleton-loader type="table-thead"/>
      <v-skeleton-loader type="table-tbody"/>
    </template>
    <p v-else-if="$fetchState.error">
      {{ $fetchState.error.response.data.status }} {{ $fetchState.error.response.data.error }}
      {{ $fetchState.error.response.data.message }}
    </p>
    <template v-else>
      <v-sheet outlined>
        <v-calendar
          ref="calendar"
          color="primary"
          :events="timetable"
          :weekdays="weekdays"
          :first-interval="8"
          :interval-count="12"
          type="week"
        >
          <template v-slot:day-label-header>
            {{ '' }}
          </template>
          <template v-slot:event="{ event }">
            <div class="pl-1">
              <span class="v-event-summary">
                <strong>{{ event.courseCode }}</strong>
                ({{ event.name }})
                <br>
                9:15 - 10:45
                <br>
                {{ event.classroom }}
              </span>
            </div>
          </template>
        </v-calendar>
      </v-sheet>
    </template>
  </div>
</template>

<script>

export default {
  data: () => ({
    timetable: [],
    weekdays: [1, 2, 3, 4, 5]
  }),
  async fetch() {
    let timetable = await this.$axios.$get('/my/timetable');
    this.timetable = timetable.map(event => {
      let start = this.convertTime(event.startTime, event.dayOfWeek);
      let end = this.convertTime(event.endTime, event.dayOfWeek);

      return {
        name: event.name,
        classroom: event.classroom.name,
        courseCode: event.courseCode,
        start,
        end,
        timed: true,
      };
    });
  },

  methods: {
    convertTime(time, dayOfTheWeek) {
      let date = new Date();
      let [hours, minutes, seconds] = time.split(':');
      let dayOfWeekNumber = this.convertDay(dayOfTheWeek);
      let difference = (dayOfWeekNumber - date.getDay()) % 7;
      date.setHours(hours);
      date.setMinutes(minutes);
      date.setSeconds(seconds);
      date.setDate(date.getDate() + difference);
      return date.getTime();
    },
    convertDay(dayOfTheWeek) {
      let WEEKDAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];
      return WEEKDAYS.indexOf(dayOfTheWeek) + 1;
    }
  },
}
</script>

<style>
.v-calendar-daily__head {
  margin-right: 0 !important;
}

.v-calendar-daily__scroll-area {
  overflow: auto !important;
}
</style>
