package org.d3if0149.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0149.myapplication.model.Absensi

class AbsensiDb {
    @Database(entities = [Absensi::class], version = 1, exportSchema = false)
    abstract class AbsensiDb : RoomDatabase() {

        abstract val dao: AbsensiDao

        companion object {
            @Volatile
            private var INSTANCE: AbsensiDb? = null
            fun getInstance(context: Context): AbsensiDb {
                synchronized(this) {
                    var instance = INSTANCE
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AbsensiDb::class.java,
                            "absensi.db"
                        ).build()
                        INSTANCE = instance
                    }
                    return instance
                }
            }
        }
    }
}
//
