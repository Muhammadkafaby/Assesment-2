package org.d3if0149.myapplication.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if0149.myapplication.database.AbsensiDao
import org.d3if0149.myapplication.model.Absensi

class MainViewModel(dao: AbsensiDao) : ViewModel() {
    val data: StateFlow<List<Absensi>> = dao.getAbsensi().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()

    )
}

