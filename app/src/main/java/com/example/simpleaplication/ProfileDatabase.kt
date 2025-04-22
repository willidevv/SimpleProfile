package com.example.simpleaplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProfileDataclass::class], version = 1, exportSchema = false)
abstract class profiledatabase : RoomDatabase() {
    abstract fun ProfileDao() : ProfileDAO

    companion object {
        @Volatile
        private var Instance: profiledatabase? = null

        fun getDatabase(context: Context): profiledatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, profiledatabase::class.java, "profile_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
