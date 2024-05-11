package org.d3if0149.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.d3if0149.myapplication.model.Absensi
import kotlinx.coroutines.flow.Flow

@Dao
interface AbsensiDao {
    @Insert
    suspend fun insert(Absensi: Absensi)

    @Update
    suspend fun update(Absensi: Absensi)

    @Query("SELECT * FROM Absensi ORDER BY tanggal DESC")
    fun getAbsensi(): Flow<List<Absensi>>

    @Query("SELECT * FROM Absensi WHERE id = :id")
    suspend fun getAbsensiById(id: Long): Absensi?

    @Query("DELETE FROM Absensi WHERE id = :id")
    suspend fun deleteById(id: Long)
}
