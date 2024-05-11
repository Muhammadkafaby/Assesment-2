package org.d3if0149.myapplication.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0149.myapplication.database.AbsensiDao
import org.d3if0149.myapplication.model.Absensi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: AbsensiDao) : ViewModel(){

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(nama: String, absen: String){
        val Absensi = Absensi(
            tanggal = formatter.format(Date()),
            nama = nama,
            absen = absen
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(Absensi)
        }
    }


    suspend fun getAbsensi(id: Long): Absensi? {
        return dao.getAbsensiById(id)
    }

    fun update(id: Long, nama: String, absen: String){
        val Absensi = Absensi(
            id = id,
            tanggal = formatter.format(Date()),
            nama = nama,
            absen = absen
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(Absensi)
        }
    }
    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
}
}
}