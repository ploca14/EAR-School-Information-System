<template>
  <v-app dark>
    <v-navigation-drawer
      mobile-breakpoint="sm"
      v-model="drawer"
      :mini-variant="!pinned && mini"
      @mouseenter.native="mini = false"
      @mouseleave.native="mini = true"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in menuItems"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title v-text="item.title" />
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <template v-slot:append>
        <v-list dense>
          <v-list-item @click.stop="pinned = !pinned">
            <template v-if="pinned">
              <v-list-item-action>
                <v-icon>mdi-pin-off</v-icon>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-content>
                  Unpin
                </v-list-item-content>
              </v-list-item-content>
            </template>
            <template v-else>
              <v-list-item-action>
                <v-icon>mdi-pin</v-icon>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-content>
                  Pin
                </v-list-item-content>
              </v-list-item-content>
            </template>
          </v-list-item>
        </v-list>
      </template>
    </v-navigation-drawer>
    <v-app-bar
      fixed
      app
    >
      <v-app-bar-nav-icon class="d-sm-none"/>
      <v-toolbar-title v-text="title" />
      <v-spacer />
      <v-btn @click="$auth.logout()">Log out</v-btn>
    </v-app-bar>
    <v-main>
      <v-container>
        <Nuxt />
      </v-container>
    </v-main>
    <v-footer app>
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  data () {
    return {
      mini: true,
      pinned: false,
      drawer: true,
      items: [
        {
          icon: 'mdi-home',
          title: 'Home',
          to: '/',
          enabled: true,
        },
        {
          icon: 'mdi-timetable',
          title: 'Timetable',
          to: '/timetable',
          enabled: true,
        },
        {
          icon: 'mdi-book-plus',
          title: 'Courses',
          to: '/course-enroll',
          enabled: true,
        },
        {
          icon: 'mdi-book-education',
          title: 'My Courses',
          to: '/courses',
          enabled: true,
        },
        {
          icon: 'mdi-account-plus',
          title: 'Add user',
          to: '/register',
          enabled: this.$auth.hasScope('ADMIN'),
        },
        {
          icon: 'mdi-book-plus-multiple',
          title: 'Add course',
          to: '/new-course',
          enabled: this.$auth.hasScope('ADMIN') || this.$auth.hasScope('STUDY_DEPARTMENT_EMPLOYEE'),
        }
      ],
      title: 'School Information System'
    }
  },

  computed: {
    menuItems() {
      return this.items.filter(item => item.enabled);
    }
  }
}
</script>
