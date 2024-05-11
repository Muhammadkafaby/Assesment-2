package org.d3if0149.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Absensi")
data class Absensi(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val absen: String,
    val tanggal: String
)
