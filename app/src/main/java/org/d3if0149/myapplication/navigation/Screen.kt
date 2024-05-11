package org.d3if0149.myapplication.navigation

import org.d3if0149.myapplication.ui.screen.KEY_ID_Absensi

sealed class Screen(val route: String) {
  data object Home: Screen("mainScreen")
  data object FormBaru: Screen("detailScreen")
  data object FormUbah: Screen("detailScreen/{$KEY_ID_Absensi}") {
    fun withId(id: Long) = "detailScreen/$id"
  }
}